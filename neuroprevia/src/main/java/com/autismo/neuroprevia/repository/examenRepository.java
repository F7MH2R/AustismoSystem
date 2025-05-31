package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface examenRepository extends JpaRepository<Examen, Integer> {
    // Filtrar por rango de edad (usamos edad en años)
    @Query("SELECT e FROM Examen e WHERE :edad BETWEEN e.edadMinima AND e.edadMaxima")
    List<Examen> findByEdad(Integer edad);

    // Filtrar por tipo de evaluación
    List<Examen> findByTipo(String tipo);
    List<Examen> findAllByCreadoPor(Usuario creadoPor);
}
