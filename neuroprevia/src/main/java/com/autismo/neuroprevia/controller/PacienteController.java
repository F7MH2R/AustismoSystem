package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.ExamenRealizado;
import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.RespuestaDada;
import com.autismo.neuroprevia.model.RespuestaPosible;
import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.model.UsuarioPrincipal;
import com.autismo.neuroprevia.model.dto.ExamenVista;
import com.autismo.neuroprevia.model.dto.PreguntaDto;
import com.autismo.neuroprevia.model.enumeration.TipoRespuesta;
import com.autismo.neuroprevia.repository.examenRealizadoRepository;
import com.autismo.neuroprevia.repository.preguntaRepository;
import com.autismo.neuroprevia.repository.respuestaDadaRepository;
import com.autismo.neuroprevia.repository.respuestaPosibleRepository;
import com.autismo.neuroprevia.service.ExamenService;
import com.autismo.neuroprevia.service.PreguntaService;
import com.autismo.neuroprevia.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Controller
@RequestMapping("/paciente")
@RequiredArgsConstructor
public class PacienteController {

    private final ExamenService                      examenService;
    private final PreguntaService                    preguntaService;
    private final UsuarioService                     usuarioService;
    private final examenRealizadoRepository          examenRealizadoRepo;
    private final respuestaPosibleRepository         respuestaPosibleRepo;
    private final respuestaDadaRepository            respuestaDadaRepo;
    private final preguntaRepository preguntaRepo;

    @GetMapping("/home")
    public String homePaciente(
            @AuthenticationPrincipal UsuarioPrincipal principal,
            Model model) {
        if (principal == null || principal.getUsuario() == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", principal.getUsuario());
        return "paciente/home";
    }

    @GetMapping("/examen/{id}")
    public String iniciarExamen(@PathVariable Integer id,
                                Model model) {
        Examen examen = examenService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Examen no encontrado"));

        List<PreguntaDto> preguntas = preguntaService
                .obtenerPreguntasPorIdExamen(id)
                .stream()
                .map(p -> PreguntaDto.builder()
                        .id(p.getId())
                        .texto(p.getTexto())
                        .tipoRespuesta(TipoRespuesta.fromValue(p.getTipoRespuesta()))
                        .respuestaPosibles(p.getRespuestaPosibles())
                        .build()
                )
                .toList();

        model.addAttribute("examen", examen);
        model.addAttribute("preguntas", preguntas);
        return "paciente/examen";
    }


    @PostMapping("/examen/{id}/submit")
    public String submitExamen(
            @PathVariable Integer id,
            @RequestParam MultiValueMap<String,String> params,
            @AuthenticationPrincipal UsuarioPrincipal principal
    ) {
        // 1) Crea el registro de ExamenRealizado (sin puntaje todavía)
        Usuario usuario = principal.getUsuario();
        ExamenRealizado er = new ExamenRealizado();
        er.setExamen(examenService.obtenerPorId(id).orElseThrow());
        er.setUsuario(usuario);
        // Lo guardamos para que tenga ID y podamos enlazar las RespuestaDada
        er = examenRealizadoRepo.save(er);

        // 2) Inicializa la suma de puntajes
        float sumaTotal = 0f;

        // 3) Recorre todas las preguntas de este examen
        List<Pregunta> todasPreguntas = preguntaService.obtenerPreguntasPorIdExamen(id);
        for (Pregunta p : todasPreguntas) {
            // Cada parámetro r{preguntaId} contiene el ID de la RespuestaPosible seleccionada
            String parametro = params.getFirst("r" + p.getId());
            if (parametro != null) {
                // 3.a) Guardamos la RespuestaDada
                RespuestaDada rd = new RespuestaDada();
                rd.setExamenRealizado(er);
                rd.setPregunta(p);

                RespuestaPosible op = respuestaPosibleRepo
                        .findById(Integer.valueOf(parametro))
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Respuesta posible no encontrada: " + parametro));

                rd.setRespuesta(op);
                respuestaDadaRepo.save(rd);
                System.out.println("→ Encontrada RespuestaPosible id=" + op.getId() + ", valorNumerico=" + op.getValorNumerico());

                // 3.b) Sumamos el valor numérico de esta RespuestaPosible
                sumaTotal += op.getValorNumerico();
            }
            System.out.println("Suma total final: " + sumaTotal);

        }

        // 4) Actualizamos el campo resultadoTotal y guardamos de nuevo ExamenRealizado
        er.setResultadoTotal(sumaTotal);
        examenRealizadoRepo.save(er);

        // 5) Redirigimos al historial para que se vea el puntaje recién calculado
        return "redirect:/paciente/historial";
    }


    @GetMapping("/examenes")
    public String verExamenes(Model model,
                              @AuthenticationPrincipal UsuarioPrincipal principal) {
        // 1) Calcula la edad del usuario
        int edad = Period.between(
                principal.getUsuario().getFechaNacimiento(),
                LocalDate.now()
        ).getYears();

        // 2) Trae los exámenes filtrados por edad (y por tipo si lo quisieras)
        List<Examen> examenesFiltrados = examenService.listar(null, null);

        // 3) Mapea cada Examen a ExamenVista, calculando número de preguntas y duración estimada
        List<ExamenVista> lista = examenesFiltrados.stream().map(e -> {
            int numPreguntas = preguntaRepo.countByExamenId(e.getId());
            int duracion = numPreguntas * 2; //
            return new ExamenVista(
                    e.getId(),
                    e.getTitulo(),
                    e.getDescripcion(),
                    e.getTipo(),
                    e.getEdadMinima(),
                    e.getEdadMaxima(),
                    numPreguntas,
                    duracion
            );
        }).toList();

        // 4) Lo pones en el modelo y devuelves la vista
        model.addAttribute("examenes", lista);
        return "paciente/examenes";
    }

    @GetMapping("/historial")
    public String historial(Model model,
                            @AuthenticationPrincipal UsuarioPrincipal principal) {
        int uid = principal.getUsuario().getId();
        List<ExamenRealizado> hechos = examenRealizadoRepo
                .findAllByUsuarioIdOrderByFechaRealizacionDesc(uid);
        model.addAttribute("historial", hechos);
        return "paciente/historial";
    }

    @GetMapping("/resultado/{rid}")
    public String resultado(
            @PathVariable("rid") Integer rid,
            Model model,
            @AuthenticationPrincipal UsuarioPrincipal principal
    ) {
        // 1) Recuperar el ExamenRealizado; si no existe, lanzar excepción o redirigir.
        ExamenRealizado er = examenRealizadoRepo.findById(rid)
                .orElseThrow(() -> new IllegalArgumentException("Informe no encontrado: " + rid));

        // 2) Comprobar que el examen realmente pertenece al usuario que está logueado
        //    (evitamos que un paciente A vea informes de paciente B)
        int usuarioId = principal.getUsuario().getId();
        if (er.getUsuario().getId() != usuarioId) {
            throw new IllegalArgumentException("No tienes permiso para ver este informe.");
        }


        // 3) Recuperar todas las respuestas dadas para este informe
        List<RespuestaDada> detalles = respuestaDadaRepo.findAllByExamenRealizado_Id(rid);

        // 4) Ya tenemos:
        //    - er: ExamenRealizado (incluye fecha, resultadoTotal, interpretación si la hubiere)
        //    - detalles: lista de RespuestaDada (cada uno vincula Pregunta + RespuestaPosible con su valorNumérico)

        model.addAttribute("informe", er);
        model.addAttribute("respuestas", detalles);

        return "paciente/resultado";
    }


    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Integer id) throws IOException {
        ExamenRealizado er = examenRealizadoRepo.findById(id).orElseThrow();

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.newLineAtOffset(50, 750);
                cs.showText("Informe: " + er.getExamen().getTitulo());
                cs.newLineAtOffset(0, -20);
                cs.showText("Paciente: " + er.getUsuario().getNombre());
                // … más líneas …
                cs.endText();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            byte[] pdfBytes = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "informe_"+id+".pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        }
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(
            @AuthenticationPrincipal UsuarioPrincipal principal,
            Model model
    ) {
        if (principal == null || principal.getUsuario() == null) {
            return "redirect:/login";
        }
        Usuario paciente = principal.getUsuario();
        model.addAttribute("paciente", paciente);
        return "paciente/perfil";
    }

    @PostMapping("/perfil")
    public String guardarPerfil(
            @ModelAttribute("paciente") Usuario form,
            @AuthenticationPrincipal UsuarioPrincipal principal
    ) {
        if (principal == null || principal.getUsuario() == null) {
            return "redirect:/login";
        }
        Usuario paciente = principal.getUsuario();

        paciente.setTelefono(form.getTelefono());
        paciente.setDireccion(form.getDireccion());
        paciente.setAlergias(form.getAlergias());
        usuarioService.guardar(paciente);

        return "redirect:/paciente/perfil?success";
    }
}