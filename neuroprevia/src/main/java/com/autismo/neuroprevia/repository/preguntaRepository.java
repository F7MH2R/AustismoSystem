package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface preguntaRepository extends JpaRepository<Pregunta, Integer> {
}
