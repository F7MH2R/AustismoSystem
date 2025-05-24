package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.model.UsuarioPrincipal;
import com.autismo.neuroprevia.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    // Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "general/login/login";
    }



    // Mostrar formulario de login
    @GetMapping("/paciente/home")
    public String mostrarhome(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        model.addAttribute("usuario", usuario);
        return "paciente/home/home";
    }
    // Mostrar formulario de login
    @GetMapping("/admin/home")
    public String mostrarhomeadmin(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        model.addAttribute("usuario", usuario);
        return "admin/dashboard/home";
    }
    // Mostrar formulario de login
    @GetMapping("/especialista/home")
    public String mostrarhomeespecialista(Model model, @AuthenticationPrincipal UsuarioPrincipal principal) {
        Usuario usuario = principal.getUsuario();
        if (usuario == null) return "redirect:/login";

        model.addAttribute("usuario", usuario);
        return "especialista/home/home";
    }

    // Mostrar formulario de registro
    @GetMapping("/registrarse")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "general/registropaciente/registro";
    }

    // Procesar registro
    @PostMapping("/registrarPaciente")
    public String registrarPaciente(@ModelAttribute Usuario usuario, Model model) {
        if (usuarioService.correoExiste(usuario.getCorreo())) {
            model.addAttribute("error", "El correo ya está registrado");
            return "general/registropaciente/registro";
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioService.registrarPaciente(usuario);
        return "redirect:/login";
    }
}
