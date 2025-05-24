package com.autismo.neuroprevia.repository;

import com.autismo.neuroprevia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface usuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByCorreoAndPassword(String correo, String password);

    long countByRolAndFechaNacimientoAfter(String rol, LocalDate fecha);

}
