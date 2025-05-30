package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface examenRepository extends JpaRepository<Examen, Integer> {
    List<Examen> findAllByCreadoPor(Usuario creadoPor);
}
