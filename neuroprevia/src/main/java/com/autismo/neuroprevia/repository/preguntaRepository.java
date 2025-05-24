package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface preguntaRepository extends JpaRepository<Pregunta, Integer> {
    List<Pregunta> findAllByExamenIdOrderByOrdenAsc(Integer idExamen);
}
