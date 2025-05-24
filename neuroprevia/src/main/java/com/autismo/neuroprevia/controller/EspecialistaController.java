package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.ExamenRealizado;
import com.autismo.neuroprevia.model.Seguimiento;
import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.model.dto.InformeDto;
import com.autismo.neuroprevia.model.dto.RespuestaDetalleDto;
import com.autismo.neuroprevia.service.DoctorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class EspecialistaController {

    @Autowired
    private DoctorService doctorService;

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
