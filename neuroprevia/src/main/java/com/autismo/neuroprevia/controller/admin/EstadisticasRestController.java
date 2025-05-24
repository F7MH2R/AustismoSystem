package com.autismo.neuroprevia.controller.admin;

import com.autismo.neuroprevia.service.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasRestController {

    @Autowired
    private EstadisticasService service;

    // DOCTORES
    @GetMapping("/doctores/total")
    public long totalDoctores() {
        return service.totalDoctores();
    }

    @GetMapping("/doctores/genero")
    public Map<String, Long> generoDoctores() {
        return service.generoDoctores();
    }

    // ADMIN
    @GetMapping("/admin/total")
    public long totalAdmins() {
        return service.totalAdmins();
    }

    @GetMapping("/admin/riesgo")
    public Map<String, Long> riesgo() {
        return service.nivelRiesgo();
    }

    // PACIENTES
    @GetMapping("/pacientes/examenes/mes")
    public Map<String, Long> examenesPorMes(@RequestParam int mes) {
        return service.examenesPorMes(mes);
    }

    @GetMapping("/pacientes/examenes/genero")
    public Map<String, Long> examenesPorGenero() {
        return service.examenesPorGeneroPacientes();
    }

    @GetMapping("/pacientes/examenes/edad")
    public Map<String, Long> examenesPorEdad() {
        return service.examenesPorEdadPacientes();
    }

    @GetMapping("/pacientes/nuevos")
    public Map<String, Long> pacientesPorDia(@RequestParam String desde, @RequestParam String hasta) {
        LocalDate d1 = LocalDate.parse(desde);
        LocalDate d2 = LocalDate.parse(hasta);
        return service.pacientesPorDia(d1, d2);
    }
}
