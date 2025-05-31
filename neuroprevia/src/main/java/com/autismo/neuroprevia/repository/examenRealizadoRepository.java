package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.ExamenRealizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface examenRealizadoRepository extends JpaRepository<ExamenRealizado, Integer> {
    List<ExamenRealizado> findAllByUsuarioIdOrderByFechaRealizacionDesc(int usuarioId);

    //FER
    long count();
    List<ExamenRealizado> findByUsuario_Rol(String rol);
    List<ExamenRealizado> findByUsuario_RolAndFechaRealizacionBetween(String rol, LocalDate desde, LocalDate hasta);
    //FER
    List<ExamenRealizado> findAll();


    List<ExamenRealizado> findAllByExamenIn(List<Examen> examenes);
}
