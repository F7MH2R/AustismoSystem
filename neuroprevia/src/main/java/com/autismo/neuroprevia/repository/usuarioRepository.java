package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface usuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByCorreoAndPassword(String correo, String password);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol = :rol AND u.creadoEn > :fecha")
    long countByRolAndCreadoEnAfter(@Param("rol") String rol, @Param("fecha") Timestamp fecha);


    //Fer
    List<Usuario> findByRol(String paciente);
    Long countByGenero(String otro);
    long countByRol(String rol);
    long countByRolAndGenero(String rol, String genero);
    List<Usuario> findByRolAndFechaNacimientoBetween(String rol, LocalDate desde, LocalDate hasta);
    //Fer
}
