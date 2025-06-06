package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.dto.RespuestaPosibleDto;
import com.autismo.neuroprevia.service.PreguntaService;
import com.autismo.neuroprevia.service.RespuestaPosibleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/respuesta")
@RequiredArgsConstructor
@Slf4j
public class RespuestaPosibleController {
    private final RespuestaPosibleService service;
    private final PreguntaService preguntaService;

    @PostMapping("/agregar")
    private String agregar(RespuestaPosibleDto dto, @RequestParam("idPregunta") int idPregunta, RedirectAttributes attributes) {
        Optional<Pregunta> pregunta = preguntaService.obtenerPorId(idPregunta);
        if(pregunta.isEmpty()) {
            attributes.addFlashAttribute("mensajeError", "Pregunta no encontrada");
        } else {
            service.crear(dto, pregunta.get().getId());
            attributes.addFlashAttribute("mensaje", "Opcion agregada con exito");
        }
        return "redirect:/preguntas/".concat(String.valueOf(idPregunta));
    }
}
