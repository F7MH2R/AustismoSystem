package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.model.UsuarioPrincipal;
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

    @GetMapping("/home")
    public String homePaciente(
            @AuthenticationPrincipal UsuarioPrincipal principal,
            Model model) {
        if (principal == null || principal.getUsuario() == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", principal.getUsuario());
        // Thymeleaf buscará: templates/paciente/home.html
        return "paciente/home";
    }

    @GetMapping("/perfil")
    public String perfil(
            @AuthenticationPrincipal UsuarioPrincipal principal,
            Model model) {
        model.addAttribute("paciente", principal.getUsuario());
        return "paciente/perfil";
    }

    @GetMapping("/examenes")
    public String examenes() {
        return "paciente/examenes";
    }

    @GetMapping("/examen/{id}")
    public String examen(@PathVariable Long id, Model model) {
        model.addAttribute("examenId", id);
        return "paciente/examen";
    }

    @PostMapping("/examen/{id}/submit")
    public String submitExamen(@PathVariable Long id,
                               @RequestParam Map<String,String> respuestas) {
        // aquí procesas respuestas…
        return "redirect:/paciente/historial";
    }

    @GetMapping("/historial")
    public String historial() {
        return "paciente/historial";
    }

    @GetMapping("/resultado/{rid}")
    public String resultado(@PathVariable Long rid, Model model) {
        model.addAttribute("resultadoId", rid);
        return "paciente/resultado";
    }
}
