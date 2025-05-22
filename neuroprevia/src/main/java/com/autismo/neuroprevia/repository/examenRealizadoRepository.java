package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.ExamenRealizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface examenRealizadoRepository extends JpaRepository<ExamenRealizado, Integer> {
}
