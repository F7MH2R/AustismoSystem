package com.autismo.neuroprevia.service;

import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.RespuestaPosible;
import com.autismo.neuroprevia.model.dto.RespuestaPosibleDto;
import com.autismo.neuroprevia.repository.preguntaRepository;
import com.autismo.neuroprevia.repository.respuestaPosibleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RespuestaPosibleService {
    private final respuestaPosibleRepository repository;
    private final preguntaRepository preguntaRepository;

    public RespuestaPosible crear(RespuestaPosibleDto dto, int idPregunta) {
        RespuestaPosible entity = new RespuestaPosible();
        Optional<Pregunta> pregunta = preguntaRepository.findById(idPregunta);

        if (pregunta.isEmpty())
            throw new EntityNotFoundException("Pregunta no encontrada");

        entity.setTextoRespuesta(dto.getTextoRespuesta());
        entity.setValorNumerico(dto.getValorNumerico());
        entity.setPregunta(pregunta.get());

        return repository.save(entity);
    }

    public List<RespuestaPosible> obtenerRespuestasPosiblesPorPregunta(Pregunta pregunta) {
        return repository.findAllByPregunta(pregunta);
    }
}
