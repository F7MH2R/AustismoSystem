package com.autismo.neuroprevia.controller.admin;

import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    @Autowired
    private usuarioRepository usuarioRepository;

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
        if (usuario.getRol().equals("DOCTOR")) {
            usuario.setVerificado(false); // campo adicional que debes tener
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
        usuarioRepository.save(usuario);
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
