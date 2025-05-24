package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.dto.PreguntaDto;
import com.autismo.neuroprevia.model.enumeration.TipoRespuesta;
import com.autismo.neuroprevia.service.ExamenService;
import com.autismo.neuroprevia.service.PreguntaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/preguntas")
public class PreguntaController {
    private final PreguntaService service;
    private final ExamenService examenService;

    @GetMapping("/")
    private String index(Model model) {
        model.addAttribute("pregunta", PreguntaDto.builder().build());
        model.addAttribute("tiposRespuesta", TipoRespuesta.values());
        return "pregunta/index";
    }

    @PostMapping("/crear")
    private String crear(@ModelAttribute("pregunta") PreguntaDto pregunta, RedirectAttributes attributes, @RequestParam("idExamen") int idExamen) {
        Optional<Examen> entity = examenService.obtenerPorId(idExamen);
        if (entity.isEmpty()) {
            attributes.addFlashAttribute("mensajeError", "Examen no valido");
            return "redirect:/examenes/crear";
        }
        else {
            attributes.addFlashAttribute("examen", entity);
            return "redirect:/examenes/ver/".concat(String.valueOf(entity.get().getId())).concat("/preguntas");

        }
    }
}
