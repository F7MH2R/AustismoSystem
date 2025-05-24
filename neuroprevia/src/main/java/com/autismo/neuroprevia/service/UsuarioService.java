package com.autismo.neuroprevia.service;

import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private usuarioRepository usuarioRepository;

    public boolean correoExiste(String correo) {
        return usuarioRepository.findByCorreo(correo).isPresent();
    }

    public Usuario registrarPaciente(Usuario usuario) {
        usuario.setRol("PACIENTE");
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> autenticar(String correo, String password) {
        return usuarioRepository.findByCorreoAndPassword(correo, password);
    }

    public Optional<Usuario> obtenerPorEmail(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}
