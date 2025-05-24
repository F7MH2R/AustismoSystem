package com.autismo.neuroprevia.controller.admin;

import com.autismo.neuroprevia.repository.usuarioRepository;
import com.autismo.neuroprevia.repository.examenRealizadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class EstadisticasController {

    @Autowired
    private examenRealizadoRepository examenRealizadoRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @GetMapping("/admin/estadisticas")
    public String verEstadisticas(Model model) {

        // Total de exámenes
        long totalExamenes = examenRealizadoRepository.count();

        // Total de pacientes nuevos esta semana
        LocalDate hace7dias = LocalDate.now().minusDays(7);
        long nuevosPacientes = usuarioRepository.countByRolAndFechaNacimientoAfter("PACIENTE", hace7dias);

        // Total de doctores
        long totalDoctores = usuarioRepository.countByRol("DOCTOR");

        // Total de administradores
        long totalAdmins = usuarioRepository.countByRol("ADMIN");

        model.addAttribute("totalExamenes", totalExamenes);
        model.addAttribute("nuevosPacientes", nuevosPacientes);
        model.addAttribute("totalDoctores", totalDoctores);
        model.addAttribute("totalAdmins", totalAdmins);

        return "admin/estadisticas/index";
    }
}
