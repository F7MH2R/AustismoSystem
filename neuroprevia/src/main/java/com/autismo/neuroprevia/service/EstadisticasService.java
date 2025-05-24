package com.autismo.neuroprevia.service;

import com.autismo.neuroprevia.model.ExamenRealizado;
import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.repository.examenRealizadoRepository;
import com.autismo.neuroprevia.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class EstadisticasService {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private examenRealizadoRepository examenRealizadoRepository;

    // DOCTORES
    public long totalDoctores() {
        return usuarioRepository.countByRol("DOCTOR");
    }

    public Map<String, Long> generoDoctores() {
        Map<String, Long> map = new HashMap<>();
        map.put("Masculino", usuarioRepository.countByRolAndGenero("DOCTOR", "M"));
        map.put("Femenino", usuarioRepository.countByRolAndGenero("DOCTOR", "F"));
        map.put("Otro", usuarioRepository.countByRolAndGenero("DOCTOR", "Otro"));
        return map;
    }

    // ADMINISTRADORES
    public long totalAdmins() {
        return usuarioRepository.countByRol("ADMIN");
    }

    public Map<String, Long> nivelRiesgo() {
        Map<String, Long> map = new HashMap<>();
        List<ExamenRealizado> exs = examenRealizadoRepository.findAll();
        map.put("Bajo", exs.stream().filter(e -> e.getInterpretacion().contains("bajo")).count());
        map.put("Medio", exs.stream().filter(e -> e.getInterpretacion().contains("medio")).count());
        map.put("Alto", exs.stream().filter(e -> e.getInterpretacion().contains("alto")).count());
        return map;
    }

    // PACIENTES
    public Map<String, Long> examenesPorMes(int mes) {
        Map<String, Long> map = new HashMap<>();
        List<ExamenRealizado> exs = examenRealizadoRepository.findByUsuario_Rol("PACIENTE");
        exs.stream().filter(e -> e.getFechaRealizacion().getMonth() == mes)
                .forEach(e -> {
                    String key = e.getFechaRealizacion().toLocalDateTime().toString();
                    map.put(key, map.getOrDefault(key, 0L) + 1);
                });
        return map;
    }

    public Map<String, Long> examenesPorGeneroPacientes() {
        Map<String, Long> map = new HashMap<>();
        List<ExamenRealizado> exs = examenRealizadoRepository.findByUsuario_Rol("PACIENTE");
        for (ExamenRealizado e : exs) {
            String genero = e.getUsuario().getGenero();
            map.put(genero, map.getOrDefault(genero, 0L) + 1);
        }
        return map;
    }

    public Map<String, Long> examenesPorEdadPacientes() {
        Map<String, Long> map = new HashMap<>();
        LocalDate hoy = LocalDate.now();
        List<ExamenRealizado> exs = examenRealizadoRepository.findByUsuario_Rol("PACIENTE");

        for (ExamenRealizado e : exs) {
            Usuario u = e.getUsuario();
            if (u.getFechaNacimiento() == null) continue;

            int edad = hoy.getYear() - u.getFechaNacimiento().getYear();
            String grupo = edad <= 5 ? "0-5" : edad <= 10 ? "6-10" : edad <= 15 ? "11-15" : "16+";

            map.put(grupo, map.getOrDefault(grupo, 0L) + 1);
        }
        return map;
    }

    public Map<String, Long> pacientesPorDia(LocalDate desde, LocalDate hasta) {
        Map<String, Long> map = new TreeMap<>();
        List<Usuario> pacientes = usuarioRepository.findByRolAndFechaNacimientoBetween("PACIENTE", desde, hasta);

        for (Usuario u : pacientes) {
            String fecha = u.getFechaNacimiento().toString();
            map.put(fecha, map.getOrDefault(fecha, 0L) + 1);
        }

        return map;
    }
}
