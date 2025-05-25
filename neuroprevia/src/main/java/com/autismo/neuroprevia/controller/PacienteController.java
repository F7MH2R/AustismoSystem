package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.repository.examenRealizadoRepository;
import com.autismo.neuroprevia.repository.respuestaPosibleRepository;
import com.autismo.neuroprevia.repository.respuestaDadaRepository;

import com.autismo.neuroprevia.model.*;
import com.autismo.neuroprevia.model.dto.PreguntaDto;
import com.autismo.neuroprevia.model.enumeration.TipoRespuesta;
import com.autismo.neuroprevia.service.ExamenService;
import com.autismo.neuroprevia.service.PreguntaService;
import com.autismo.neuroprevia.service.UsuarioService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import com.autismo.neuroprevia.service.UsuarioService;
import com.autismo.neuroprevia.repository.examenRepository;
import com.autismo.neuroprevia.repository.preguntaRepository;
import com.autismo.neuroprevia.model.dto.ExamenVista;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    private final ExamenService                      examenService;
    private final PreguntaService                    preguntaService;
    private final UsuarioService                     usuarioService;
    private final examenRealizadoRepository          examenRealizadoRepo;
    private final respuestaPosibleRepository         respuestaPosibleRepo;
    private final respuestaDadaRepository            respuestaDadaRepo;
    private final examenRepository examenRepo;
    private final preguntaRepository preguntaRepo;

    @Autowired
    public PacienteController(ExamenService examenService,
                              PreguntaService preguntaService,
                              UsuarioService usuarioService,
                              examenRealizadoRepository examenRealizadoRepo,
                              respuestaPosibleRepository respuestaPosibleRepo,
                              respuestaDadaRepository respuestaDadaRepo,
                              examenRepository examenRepo,
                              preguntaRepository preguntaRepo) {
        this.examenService        = examenService;
        this.preguntaService      = preguntaService;
        this.usuarioService       = usuarioService;
        this.examenRealizadoRepo  = examenRealizadoRepo;
        this.respuestaPosibleRepo = respuestaPosibleRepo;
        this.respuestaDadaRepo    = respuestaDadaRepo;
        this.examenRepo = examenRepo;
        this.preguntaRepo = preguntaRepo;
    }


    @GetMapping("/home")
    public String homePaciente(
            @AuthenticationPrincipal UsuarioPrincipal principal,
            Model model) {
        if (principal == null || principal.getUsuario() == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", principal.getUsuario());
        // Thymeleaf buscará: templates/paciente/home.html
        return "paciente/home";
    }

    @GetMapping("/examen/{id}")
    public String iniciarExamen(@PathVariable Integer id,
                                Model model,
                                @AuthenticationPrincipal UsuarioPrincipal principal) {
        Examen examen = examenService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Examen no encontrado"));

        List<PreguntaDto> preguntas = preguntaService
                .obtenerPreguntasPorIdExamen(id)   // esto te devuelve List<Pregunta>
                .stream()
                .map(p -> PreguntaDto.builder()
                        .id(p.getId())
                        .texto(p.getTexto())
                        .tipoRespuesta(TipoRespuesta.valueOf(p.getTipoRespuesta()))
                        .orden(p.getOrden())
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
        // 1) Crea el registro de examen_realizado
        Usuario usuario = principal.getUsuario();
        ExamenRealizado er = new ExamenRealizado();
        er.setExamen(examenService.obtenerPorId(id).get());
        er.setUsuario(usuario);
        this.examenRealizadoRepo.save(er);

        // 2) Recorre todas las preguntas y sus respuestas enviadas
        preguntaService.obtenerPreguntasPorIdExamen(id).forEach(p -> {
            String param = params.getFirst("r" + p.getId());
            if (param != null) {
                RespuestaDada rd = new RespuestaDada();
                rd.setExamenRealizado(er);
                rd.setPregunta(p);
                // obtén la respuesta posible por su id
                RespuestaPosible op = respuestaPosibleRepo
                        .findById(Integer.valueOf(param))
                        .orElseThrow();
                rd.setRespuesta(op);
                this.respuestaDadaRepo.save(rd);
            }
        });

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
        List<Examen> examenesFiltrados = examenService.listar(edad, null);

        // 3) Mapea cada Examen a ExamenVista, calculando número de preguntas y duración estimada
        List<ExamenVista> lista = examenesFiltrados.stream().map(e -> {
            int numPreguntas = preguntaRepo.countByExamenId(e.getId());
            int duracion = numPreguntas * 2; // p.ej. 2 minutos por pregunta
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
    public String resultado(@PathVariable Long rid, Model model) {
        model.addAttribute("resultadoId", rid);
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