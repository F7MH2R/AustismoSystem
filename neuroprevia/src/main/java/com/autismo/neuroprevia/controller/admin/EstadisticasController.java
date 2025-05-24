package com.autismo.neuroprevia.controller.admin;

import com.autismo.neuroprevia.repository.examenRealizadoRepository;
import com.autismo.neuroprevia.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class EstadisticasController {

    @Autowired
    private examenRealizadoRepository examenRealizadoRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @GetMapping("/admin/estadisticas")
    public String verEstadisticas(Model model) {

        // 1. Total de exámenes aplicados
        long totalExamenes = examenRealizadoRepository.count();

        // 2. Pacientes nuevos esta semana
        LocalDate hace7dias = LocalDate.now().minusDays(7);
        long nuevosPacientes = usuarioRepository.countByRolAndFechaNacimientoAfter("PACIENTE", hace7dias);

        model.addAttribute("totalExamenes", totalExamenes);
        model.addAttribute("nuevosPacientes", nuevosPacientes);

        // Los datos para gráficos pueden cargarse vía AJAX o también desde acá si ya están agrupados

        return "admin/estadisticas/index";
    }
}
