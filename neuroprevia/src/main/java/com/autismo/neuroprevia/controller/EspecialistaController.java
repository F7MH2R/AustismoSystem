package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.*;
import com.autismo.neuroprevia.model.dto.InformeDto;
import com.autismo.neuroprevia.model.dto.RespuestaDetalleDto;
import com.autismo.neuroprevia.repository.examenRealizadoRepository;
import com.autismo.neuroprevia.repository.respuestaDadaRepository;
import com.autismo.neuroprevia.repository.respuestaPosibleRepository;
import com.autismo.neuroprevia.service.DoctorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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


    // PUNTO 4 – Informes Clínicos
    @GetMapping("/especialista/informes")
    public String verInformes(Model model, HttpSession session) {
        Usuario doctor = (Usuario) session.getAttribute("usuarioLogueado");
        if (doctor == null || !doctor.getRol().equals("DOCTOR")) return "redirect:/login";

        List<InformeDto> informes = doctorService.obtenerInformesRealizados();
        model.addAttribute("informes", informes);
        return "especialista/home/informes/lista";
        // Vista HTML
    }

    // PUNTO 4 - Ver Informes completos
    @GetMapping("/especialista/informes/{id}")
    public String verInforme(@PathVariable Long id, Model model, HttpSession session) {
        Usuario doctor = (Usuario) session.getAttribute("usuarioLogueado");
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
                                        HttpSession session) {

        Usuario doctor = (Usuario) session.getAttribute("usuarioLogueado");
        if (doctor == null || !doctor.getRol().equals("DOCTOR")) return "redirect:/login";

        // ✅ 1. Actualizar interpretación
        ExamenRealizado er = examenRealizadoRepo.findById(id.intValue()).orElseThrow();
        er.setInterpretacion(interpretacion);
        examenRealizadoRepo.save(er);

        // ✅ 2. Actualizar respuestas dadas
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

        return "redirect:/especialista/informes";
    }





    // PUNTO 5 – Agenda de Seguimiento
    @GetMapping("/especialista/seguimientos")
    public String verSeguimientos(Model model, HttpSession session) {
        Usuario doctor = (Usuario) session.getAttribute("usuarioLogueado");
        if (doctor == null || !doctor.getRol().equals("DOCTOR")) return "redirect:/login";

        List<Seguimiento> seguimientos = doctorService.obtenerSeguimientosDelDoctor(doctor.getId());
        model.addAttribute("seguimientos", seguimientos);
        return "especialista/seguimientos/lista"; // Vista HTML
    }
}
