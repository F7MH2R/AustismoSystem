package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.Seguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface seguimientoRepository extends JpaRepository<Seguimiento, Integer> {
}
