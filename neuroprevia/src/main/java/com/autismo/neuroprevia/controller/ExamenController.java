package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.model.UsuarioPrincipal;
import com.autismo.neuroprevia.model.dto.ExamenDto;
import com.autismo.neuroprevia.model.dto.PreguntaDto;
import com.autismo.neuroprevia.model.enumeration.TipoExamen;
import com.autismo.neuroprevia.model.enumeration.TipoRespuesta;
import com.autismo.neuroprevia.service.ExamenService;
import com.autismo.neuroprevia.service.PreguntaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/examenes")
@Slf4j
public class ExamenController {

    private final ExamenService service;
    private final PreguntaService preguntaService;

    @GetMapping("/crear")
    public String index(Model model) {
        model.addAttribute("tipos", TipoExamen.values());
        model.addAttribute("examen", ExamenDto.builder().build());
        return "examen/index";
    }

    @PostMapping("/crear")
    public String crear(@ModelAttribute("examen") ExamenDto examen, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        Usuario usuario = usuarioPrincipal.getUsuario();

        if(examen.getEdadMaxima() <= examen.getEdadMinima()) {
            attributes.addFlashAttribute("mensaje", "Error en el rango de edad");
        }

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

        List<Pregunta> preguntas = preguntaService.obtenerPreguntasPorIdExamen(idExamen);

        model.addAttribute("pregunta", PreguntaDto.builder().build());
        model.addAttribute("tiposRespuesta", TipoRespuesta.values());
        model.addAttribute("examen", examen.get());
        model.addAttribute("preguntas", preguntas);
        return "examen/detalles";
    }

    @PostMapping("/ver/{id}/preguntas/agregar")
    public String agregarPregunta(@ModelAttribute("pregunta") PreguntaDto pregunta, RedirectAttributes attributes, @PathVariable("id") int idExamen) {
        log.info("Objetos recibidos: dto={}, idExamen={}", pregunta, idExamen);
        if(service.obtenerPorId(idExamen).isEmpty())
            attributes.addFlashAttribute("mensajeError", "El examen al que deseas acceder no existe");

        preguntaService.crear(pregunta, idExamen);
        attributes.addFlashAttribute("mensaje", "Pregunta agregada con exito");
        return "redirect:/examenes/ver/".concat(String.valueOf(idExamen)).concat("/preguntas");
    }

    @Autowired
    private ExamenService examenService;
    @GetMapping
    public String listar(
            @RequestParam(required=false) String grupoEdad,   // "niño" o "adulto"
            @RequestParam(required=false) String tipo,        // "general" o "específica"
            Model model)
    {
        Integer edad = null;
        if ("niño".equalsIgnoreCase(grupoEdad))  edad = 10;  // ejemplo: edad=10 representativa
        if ("adulto".equalsIgnoreCase(grupoEdad)) edad = 30;  // edad=30

        List<Examen> examenes = examenService.listar(edad, tipo);
        model.addAttribute("examenes", examenes);
        return "paciente/examenes";
    }

}
