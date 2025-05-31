package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.ExamenRealizado;
import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.RespuestaDada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface respuestaDadaRepository extends JpaRepository<RespuestaDada, Integer> {
    List<RespuestaDada> findByExamenRealizado_Id(Integer id);
    List<RespuestaDada> findAllByPreguntaInAndExamenRealizado(List<Pregunta> preguntas, ExamenRealizado examenRealizado);
    /**
     * Encuentra todas las RespuestaDada donde el ExamenRealizado.usuario.id = :usuarioId
     * y donde Pregunta.examen.id = :examenId.
     */
    List<RespuestaDada> findAllByExamenRealizado_Usuario_IdAndPregunta_Examen_Id(
            Integer usuarioId,
            Integer examenId
    );
}
