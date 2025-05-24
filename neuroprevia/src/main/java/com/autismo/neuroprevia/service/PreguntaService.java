package com.autismo.neuroprevia.service;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.dto.PreguntaDto;
import com.autismo.neuroprevia.repository.examenRepository;
import com.autismo.neuroprevia.repository.preguntaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreguntaService {
    private final preguntaRepository repository;
    private final examenRepository examenRepository;

    public Pregunta crear(PreguntaDto dto, int idExamen) {
        Pregunta entity = new Pregunta();
        Optional<Examen> examen = examenRepository.findById(idExamen);
        if(examen.isEmpty())
            throw new EntityNotFoundException("Examen no valido");

        entity.setExamen(examen.get());
        entity.setTexto(dto.getTexto());
        entity.setTipoRespuesta(dto.getTipoRespuesta().getValue());
        entity.setOrden(dto.getOrden());

        return repository.save(entity);
    }

    public List<Pregunta> obtenerPreguntasPorIdExamen(int idExamen) {
        return repository.findAllByExamenIdOrderByOrdenAsc(idExamen);
    }
}
