package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.RespuestaDada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface respuestaDadaRepository extends JpaRepository<RespuestaDada, Long> {

    /**
     * Encuentra todas las RespuestaDada donde el ExamenRealizado.usuario.id = :usuarioId
     * y donde Pregunta.examen.id = :examenId.
     */
    List<RespuestaDada> findAllByExamenRealizado_Usuario_IdAndPregunta_Examen_Id(
            Integer usuarioId,
            Integer examenId
    );
}

