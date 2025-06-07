package com.autismo.neuroprevia.controller.admin;

import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.repository.usuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {


    @Autowired
    private usuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // 1. Listar todos los usuarios
    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios/index"; // vista: templates/admin/usuarios/index.html
    }

    // 2. Mostrar formulario de creación
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/usuarios/formulario";
    }

    // 3. Procesar creación
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuario.setActivo(true);

        // Encriptar si viene una contraseña (no vacía)
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        if (usuario.getRol().equals("DOCTOR")) {
            usuario.setVerificado(false);
        }

        usuarioRepository.save(usuario);
        return "redirect:/admin/usuarios";
    }

    // 4. Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        model.addAttribute("usuario", usuario);
        return "admin/usuarios/formulario";
    }

    // 5. Procesar edición
    @PostMapping("/editar")
    public String editarUsuario(@ModelAttribute Usuario usuario) {
        // Obtener usuario actual desde DB
        Usuario existente = usuarioRepository.findById(usuario.getId()).orElseThrow();

        // Si se proporcionó una nueva contraseña, se encripta
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            existente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        // Actualizar otros campos
        existente.setNombre(usuario.getNombre());
        existente.setCorreo(usuario.getCorreo());
        existente.setRol(usuario.getRol());
        existente.setActivo(usuario.isActivo());
        existente.setVerificado(usuario.isVerificado());

        usuarioRepository.save(existente);
        return "redirect:/admin/usuarios";
    }


    // 6. Activar/Desactivar usuario
    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstado(@PathVariable int id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setActivo(!usuario.isActivo());
        usuarioRepository.save(usuario);
        return "redirect:/admin/usuarios";
    }

    // 7. Eliminar usuario
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable int id) {
        usuarioRepository.deleteById(id);
        return "redirect:/admin/usuarios";
    }
}
