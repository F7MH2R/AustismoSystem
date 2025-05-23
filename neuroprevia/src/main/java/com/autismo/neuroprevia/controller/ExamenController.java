package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.model.dto.ExamenDto;
import com.autismo.neuroprevia.model.enumeration.TipoExamen;
import com.autismo.neuroprevia.service.ExamenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/examenes")
public class ExamenController {

    private final ExamenService service;

    @GetMapping("/crear")
    public String index(Model model) {
        model.addAttribute("tipos", TipoExamen.values());
        model.addAttribute("examen", ExamenDto.builder().build());
        return "examen/index";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute("examen") ExamenDto examen, RedirectAttributes attributes, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        Examen entity = service.crearExamen(examen, usuario.getId());
        attributes.addFlashAttribute("examen", entity);
        return "redirect:/examenes/ver/".concat(String.valueOf(entity.getId())).concat("/preguntas");
    }

    @GetMapping("/ver/{id}/preguntas")
    public String crearPreguntas(Model model, @PathVariable("id") int idExamen) {
        Optional<Examen> examen = service.obtenerPorId(idExamen);
        if(examen.isEmpty()) {
            model.addAttribute("mensajeError", "Examen no encontrado");
            return "examen/detalles";
        }

        model.addAttribute("examen", examen.get());
        return "examen/detalles";
    }
}
