package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.Seguimiento;
import com.autismo.neuroprevia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface seguimientoRepository extends JpaRepository<Seguimiento, Integer> {
    List<Seguimiento> findByIdDoctor(int idDoctor);

    boolean existsByIdDoctorAndPacienteAndEstado(int idDoctor, Usuario paciente, String estado);

    boolean existsByIdDoctorAndFechaCita(int idDoctor, LocalDate fecha);
}


