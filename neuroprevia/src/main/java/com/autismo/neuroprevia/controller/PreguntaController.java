package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.RespuestaPosible;
import com.autismo.neuroprevia.model.dto.PreguntaDto;
import com.autismo.neuroprevia.model.enumeration.TipoRespuesta;
import com.autismo.neuroprevia.service.ExamenService;
import com.autismo.neuroprevia.service.PreguntaService;
import com.autismo.neuroprevia.service.RespuestaPosibleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/preguntas")
@Slf4j
public class PreguntaController {
    private final PreguntaService service;
    private final ExamenService examenService;
    private final RespuestaPosibleService respuestaPosibleService;

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

    @GetMapping("/{id}")
    private String verPregunta(@PathVariable("id") int idPregunta, Model model) {
        log.info("Ver pregunta: id={}", idPregunta);
        Optional<Pregunta> entity = service.obtenerPorId(idPregunta);
        if (entity.isEmpty()) {
            log.warn("Pregunta no encontrada: id={}", idPregunta);
            model.addAttribute("mensajeError", "Pregunta no valida");
        } else {
            log.info("Se ha encontrado la siguiente pregunta: pregunta={}", entity.map(Pregunta::getTexto).orElse(""));
            List<RespuestaPosible> respuestas = respuestaPosibleService.obtenerRespuestasPosiblesPorPregunta(entity.get());
            model.addAttribute("pregunta", entity.get());
            model.addAttribute("respuestas", respuestas);
        }
        return "pregunta/detalles";
    }

}
