package com.autismo.neuroprevia.service;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.ExamenRealizado;
import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.RespuestaDada;
import com.autismo.neuroprevia.repository.respuestaDadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RespuestaDadaService {
    private final respuestaDadaRepository repository;

    public List<RespuestaDada> obtenerRespuestasPorPreguntasYExamenRealizado(List<Pregunta> preguntas, ExamenRealizado examenRealizado) {
        return repository.findAllByPreguntaInAndExamenRealizado(preguntas, examenRealizado);
    }
}
