package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/home")
    public String homePaciente(@AuthenticationPrincipal UserDetails principal,
                               Model model) {
        Usuario usuario = usuarioService
                .findByCorreo(principal.getUsername())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "paciente/home";
    }

    @GetMapping("/perfil")
    public String perfil(@AuthenticationPrincipal UserDetails principal,
                         Model model) {
        Usuario usuario = usuarioService
                .findByCorreo(principal.getUsername())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        model.addAttribute("paciente", usuario);
        return "paciente/perfil";
    }

    @GetMapping("/examenes")
    public String examenes(@AuthenticationPrincipal UserDetails principal,
                           Model model) {
        return "paciente/examenes";
    }

    @GetMapping("/examen/{id}")
    public String examen(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails principal,
                         Model model) {
        return "paciente/examen";
    }

    @PostMapping("/examen/{id}/submit")
    public String submitExamen(@PathVariable Long id,
                               @RequestParam Map<String,String> respuestas,
                               @AuthenticationPrincipal UserDetails principal) {
        return "redirect:/paciente/historial";
    }

    @GetMapping("/historial")
    public String historial(@AuthenticationPrincipal UserDetails principal,
                            Model model) {
        return "paciente/historial";
    }

    @GetMapping("/resultado/{rid}")
    public String resultado(@PathVariable Long rid,
                            @AuthenticationPrincipal UserDetails principal,
                            Model model) {
        return "paciente/resultado";
    }
}
