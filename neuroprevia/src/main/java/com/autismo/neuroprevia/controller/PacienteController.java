package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/paciente")
public class PacienteController {

    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        model.addAttribute("paciente", usuario);
        return "paciente/perfil";
    }

    @GetMapping("/examenes")
    public String examenes(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        return "paciente/examenes";
    }

    @GetMapping("/examen/{id}")
    public String examen(@PathVariable Long id, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        return "paciente/examen";
    }

    @PostMapping("/examen/{id}/submit")
    public String submitExamen(@PathVariable Long id,
                               @RequestParam Map<String,String> respuestas,
                               HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        return "redirect:/paciente/historial";
    }

    @GetMapping("/historial")
    public String historial(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        return "paciente/historial";
    }

    @GetMapping("/resultado/{rid}")
    public String resultado(@PathVariable Long rid, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) return "redirect:/login";

        return "paciente/resultado";
    }
}
