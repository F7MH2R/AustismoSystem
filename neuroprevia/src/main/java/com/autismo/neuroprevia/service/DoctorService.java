package com.autismo.neuroprevia.service;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.ExamenRealizado;
import com.autismo.neuroprevia.model.Seguimiento;
import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.model.dto.InformeDto;
import com.autismo.neuroprevia.repository.examenRealizadoRepository;
import com.autismo.neuroprevia.repository.examenRepository;
import com.autismo.neuroprevia.repository.seguimientoRepository;
import com.autismo.neuroprevia.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private examenRealizadoRepository examenRealizadoRepo;

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private examenRepository examenRepository;

    @Autowired
    private seguimientoRepository seguimientoRepo;

    public List<InformeDto> obtenerInformesRealizados() {
        List<ExamenRealizado> realizados = examenRealizadoRepo.findAll();

        return realizados.stream().map(er -> {
            InformeDto dto = new InformeDto();

            dto.setId((long) er.getId()); // si InformeDto espera Long
            dto.setFechaRealizacion(er.getFechaRealizacion().toLocalDateTime());
            dto.setResultadoTotal((double) er.getResultadoTotal());
            dto.setInterpretacion(er.getInterpretacion());

            Usuario paciente = er.getUsuario();
            dto.setNombrePaciente(paciente != null ? paciente.getNombre() + " " + paciente.getApellido() : "Desconocido");

            Examen examen = er.getExamen();
            dto.setExamenTitulo(examen != null ? examen.getTitulo() : "Sin título");

            return dto;
        }).toList();
    }


    public List<Seguimiento> obtenerSeguimientosDelDoctor(int idDoctor) {
        return seguimientoRepo.findByIdDoctor(idDoctor);
    }
}

