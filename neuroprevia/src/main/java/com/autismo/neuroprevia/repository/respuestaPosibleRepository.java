package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.RespuestaPosible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface respuestaPosibleRepository extends JpaRepository<RespuestaPosible, Integer> {
    List<RespuestaPosible> findByPregunta(Pregunta pregunta);

}
