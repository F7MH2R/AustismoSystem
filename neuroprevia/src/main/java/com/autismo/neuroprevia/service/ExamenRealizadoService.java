package com.autismo.neuroprevia.service;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.ExamenRealizado;
import com.autismo.neuroprevia.repository.examenRealizadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamenRealizadoService {
    private final examenRealizadoRepository repository;

    public List<ExamenRealizado> obtenerExamenesRealizadosPorListaId(List<Examen> examenes) {
        return repository.findAllByExamenIn(examenes);
    }
}
