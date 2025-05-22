package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller

public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

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
    public String mostrarhomeespecialista(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        model.addAttribute("usuario", usuario);
        return "especialista/home/home";
    }

    // Procesar login
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                @RequestParam String password,
                                Model model,
                                HttpSession session) {

        return usuarioService.autenticar(correo, password)
                .map(usuario -> {
                    session.setAttribute("usuarioLogueado", usuario);

                    switch (usuario.getRol()) {
                        case "PACIENTE": return "redirect:/paciente/home";
                        case "DOCTOR": return "redirect:/especialista/home";
                        case "ADMIN": return "redirect:/admin/home";
                        default: return "redirect:/login";
                    }
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Correo o contraseña incorrectos");
                    return "general/login/login";
                });
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
        usuarioService.registrarPaciente(usuario);
        return "redirect:/login";
    }
}
