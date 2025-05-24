package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping({"/", "/login"})
    public String mostrarLogin(@RequestParam(value="error", required=false) String error,
                               @RequestParam(value="logout", required=false) String logout,
                               Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Usuario o contraseña inválidos");
        }
        if (logout != null) {
            model.addAttribute("msg", "Has cerrado sesión correctamente");
        }
        return "general/login/login";
    }

    @GetMapping("/registrarse")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "general/registropaciente/registro";
    }

    @PostMapping("/registrarPaciente")
    public String registrarPaciente(
            @ModelAttribute("usuario") Usuario usuario,
            Model model) {

        System.out.println(">>> EN CONTROLLER, datos: " + usuario);
        System.out.println(">> Datos recibidos: " + usuario);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario saved = usuarioService.registrarPaciente(usuario);
        System.out.println(">> Guardado ID=" + saved.getId());
        return "redirect:/login";
    }

}
