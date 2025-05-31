package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.*;
import com.autismo.neuroprevia.model.dto.InformeDto;
import com.autismo.neuroprevia.model.dto.RespuestaDetalleDto;
import com.autismo.neuroprevia.repository.*;
import com.autismo.neuroprevia.service.DoctorService;
import com.autismo.neuroprevia.service.PdfService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class EspecialistaController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private examenRealizadoRepository examenRealizadoRepo;

    @Autowired
    private respuestaDadaRepository respuestaDadaRepo;

    @Autowired
    private respuestaPosibleRepository respuestaPosibleRepo;

    @Autowired
    private seguimientoRepository seguimientoRepo;

    @Autowired
    private usuarioRepository usuarioRepo;

    // PUNTO 4 – Informes Clínicos
    @GetMapping("/especialista/informes")
    public String verInformes(Model model, @AuthenticationPrincipal UsuarioPrincipal principal) {
        Usuario doctor = principal.getUsuario();
        if (doctor == null || !doctor.getRol().equals("DOCTOR")) return "redirect:/login";

        List<InformeDto> informes = doctorService.obtenerInformesRealizados();
        List<Seguimiento> seguimientos = doctorService.obtenerSeguimientosDelDoctor(doctor.getId());

        model.addAttribute("informes", informes != null ? informes : new ArrayList<>());
        model.addAttribute("seguimientos", seguimientos != null ? seguimientos : new ArrayList<>());
        return "especialista/home/informes/lista";
    }


    // PUNTO 4 - Ver Informes completos
    @GetMapping("/especialista/informes/{id}")
    public String verInforme(@PathVariable Long id, Model model, @AuthenticationPrincipal UsuarioPrincipal principal) {
        Usuario doctor = principal.getUsuario();
        if (doctor == null || !doctor.getRol().equals("DOCTOR")) return "redirect:/login";

        InformeDto informe = doctorService.obtenerInformePorId(id);
        List<RespuestaDetalleDto> respuestas = doctorService.obtenerRespuestasPorInforme(id);

        model.addAttribute("informe", informe);
        model.addAttribute("respuestas", respuestas);
        return "especialista/home/informes/ver";
    }

    //Punto 4 -Editar
    @GetMapping("/especialista/informes/{id}/editar")
    public String editarTodo(@PathVariable Long id, Model model) {
        ExamenRealizado er = examenRealizadoRepo.findById(id.intValue()).orElseThrow();
        String interpretacion = er.getInterpretacion();

        List<RespuestaDetalleDto> detalles = doctorService.obtenerRespuestasConOpciones(id);
        model.addAttribute("respuestas", detalles);
        model.addAttribute("interpretacion", interpretacion);
        model.addAttribute("idExamenRealizado", id);
        return "especialista/home/informes/editar";
    }

    @PostMapping("/especialista/informes/{id}/guardar-todo")
    public String guardarCambiosInforme(@PathVariable Long id,
                                        @RequestParam("interpretacion") String interpretacion,
                                        @RequestParam("respuestasSeleccionadas") List<Integer> respuestasSeleccionadas,
                                        @AuthenticationPrincipal UsuarioPrincipal principal,
                                        RedirectAttributes redirectAttributes) {

        Usuario doctor = principal.getUsuario();
        if (doctor == null || !doctor.getRol().equals("DOCTOR")) return "redirect:/login";

        //  1. Actualizar interpretación
        ExamenRealizado er = examenRealizadoRepo.findById(id.intValue()).orElseThrow();
        er.setInterpretacion(interpretacion);
        examenRealizadoRepo.save(er);

        // 2. Actualizar respuestas dadas
        List<RespuestaDada> dadas = respuestaDadaRepo.findByExamenRealizado_Id(id.intValue());

        for (int i = 0; i < dadas.size(); i++) {
            RespuestaDada r = dadas.get(i);
            Integer nuevaRespuestaId = respuestasSeleccionadas.get(i);
            RespuestaPosible nuevaRespuesta = respuestaPosibleRepo.findById(nuevaRespuestaId).orElse(null);

            if (nuevaRespuesta != null) {
                r.setRespuesta(nuevaRespuesta);
                respuestaDadaRepo.save(r);
            }
        }

        redirectAttributes.addFlashAttribute("guardado", true);
        return "redirect:/especialista/informes";
    }

    private final PdfService pdfService;

    @Autowired
    public EspecialistaController(DoctorService doctorService, PdfService pdfService) {
        this.doctorService = doctorService;
        this.pdfService = pdfService;
    }

    // Generar el PDF
    @GetMapping("/informes/{id}/generar-pdf")
    public ResponseEntity<byte[]> generarPdf(@PathVariable("id") Integer id) {
        try {
            InformeDto informe = doctorService.obtenerInformePorId(id.longValue());
            List<RespuestaDetalleDto> respuestas = doctorService.obtenerRespuestasPorInforme(id.longValue());

            byte[] pdf = pdfService.generarInformePDF(informe, respuestas);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("informe-clinico-" + id + ".pdf")
                    .build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Crear seguimiento
    @GetMapping("/especialista/seguimiento/crear/{idExamen}")
    public String mostrarFormularioSeguimiento(@PathVariable("idExamen") Long idExamen,
                                               Model model,
                                               @AuthenticationPrincipal UsuarioPrincipal principal,
                                               RedirectAttributes redirectAttributes) {
        Usuario doctor = principal.getUsuario();
        if (doctor == null || !doctor.getRol().equals("DOCTOR")) return "redirect:/login";

        ExamenRealizado examen = examenRealizadoRepo.findById(idExamen.intValue()).orElseThrow();
        Usuario paciente = examen.getUsuario();

        if (doctorService.tieneSeguimientoPendiente(doctor.getId(), paciente.getId())) {
            redirectAttributes.addFlashAttribute("error", "Ya existe un seguimiento pendiente para este paciente.");
            return "redirect:/especialista/informes";
        }

        model.addAttribute("paciente", paciente);
        model.addAttribute("doctor", doctor);
        return "especialista/home/seguimiento/crear";
    }

    // Guardar seguimiento
    @PostMapping("/especialista/seguimiento/guardar")
    public String guardarSeguimiento(@RequestParam("idDoctor") int idDoctor,
                                     @RequestParam("idPaciente") int idPaciente,
                                     @RequestParam("fechaCita") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCita,
                                     @RequestParam("notas") String notas,
                                     RedirectAttributes redirectAttributes) {

        if (fechaCita == null || fechaCita.isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "La fecha debe ser válida y posterior a hoy.");
            return "redirect:/especialista/informes";
        }

        if (seguimientoRepo.existsByIdDoctorAndFechaCita(idDoctor, fechaCita)) {
            redirectAttributes.addFlashAttribute("error", "Ya existe otra cita agendada en esa fecha para este doctor.");
            return "redirect:/especialista/informes";
        }

        Usuario paciente = usuarioRepo.findById(idPaciente).orElseThrow(); // 👈 obtener paciente

        Seguimiento s = new Seguimiento();
        s.setIdDoctor(idDoctor);
        s.setPaciente(paciente); // 👈 asignar objeto Usuario
        s.setFechaCita(fechaCita);
        s.setNotas(notas);
        s.setEstado("Pendiente");

        seguimientoRepo.save(s);
        redirectAttributes.addFlashAttribute("seguimientoCreado", true);
        return "redirect:/especialista/home/informes/lista";
    }


    // Actualizar estado de seguimiento
    @PostMapping("/especialista/seguimiento/{id}/actualizar-estado")
    public String actualizarEstado(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        Optional<Seguimiento> optional = seguimientoRepo.findById(id);
        if (optional.isPresent()) {
            Seguimiento s = optional.get();
            s.setEstado("Realizado");
            seguimientoRepo.save(s);
            redirectAttributes.addFlashAttribute("guardado", true);
        }
        return "redirect:/especialista/informes";
    }
}
