package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.RespuestaDada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface respuestaDadaRepository extends JpaRepository<RespuestaDada, Integer> {
}
