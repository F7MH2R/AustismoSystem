package com.autismo.neuroprevia.service;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.model.dto.ExamenDto;
import com.autismo.neuroprevia.repository.examenRepository;
import com.autismo.neuroprevia.repository.usuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamenService {
    private final examenRepository repository;
    private final usuarioRepository usuarioRepository;
    private final examenRepository repo;

    public Examen crearExamen(ExamenDto dto, int creadoPor) {
        log.info("Crear examen: dto={}", dto);
        Optional<Usuario> usuario = usuarioRepository.findById(creadoPor);
        if(usuario.isEmpty())
            throw  new EntityNotFoundException("El usuario detallado no existe");

        Examen examen = new Examen();
        examen.setDescripcion(dto.getDescripcion());
        examen.setEdadMaxima(dto.getEdadMaxima());
        examen.setEdadMinima(dto.getEdadMinima());
        examen.setFechaCreacion(Timestamp.from(Instant.now()));
        examen.setTipo(dto.getTipo().getValue());
        examen.setTitulo(dto.getTitulo());
        examen.setCreadoPor(usuario.get());
        return repository.save(examen);
    }

    public Optional<Examen> obtenerPorId(int idExamen) {
        return repository.findById(idExamen);
    }

    public List<Examen> obtenerPorIdCreadoPor(Usuario usuario) {
        return repository.findAllByCreadoPor(usuario);
    }


    public List<Examen> listar(Integer edad, String tipo) {
        if (edad != null && tipo != null) {
            return repo.findByEdad(edad).stream()
                    .filter(e -> e.getTipo().equalsIgnoreCase(tipo))
                    .collect(Collectors.toList());
        } else if (edad != null) {
            return repo.findByEdad(edad);
        } else if (tipo != null) {
            return repo.findByTipo(tipo);
        }
        return repo.findAll();
    }
}
