package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.ExamenRealizado;
import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.RespuestaDada;
import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.model.UsuarioPrincipal;
import com.autismo.neuroprevia.model.enumeration.TipoExamen;
import com.autismo.neuroprevia.service.ExamenRealizadoService;
import com.autismo.neuroprevia.service.ExamenService;
import com.autismo.neuroprevia.service.PreguntaService;
import com.autismo.neuroprevia.service.RespuestaDadaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/examenes/realizados")
@RequiredArgsConstructor
@Slf4j
public class ExamenRealizadoController {
    private final ExamenRealizadoService service;
    private final ExamenService examenService;
    private final PreguntaService preguntaService;
    private final RespuestaDadaService respuestaDadaService;

    @GetMapping()
    public String index(Model model, @AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        Usuario usuario = usuarioPrincipal.getUsuario();
        List<Examen> examenesDoctor = examenService.obtenerPorIdCreadoPor(usuario);
        List<ExamenRealizado> examenesRealizados = service.obtenerExamenesRealizadosPorListaId(examenesDoctor);
        model.addAttribute("examenes", examenesDoctor);
        model.addAttribute("examenesRealizados", examenesRealizados);
        model.addAttribute("tipos", TipoExamen.values());
        return "realizados/index";
    }

    @GetMapping("/{id}")
    public String detalles(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UsuarioPrincipal usuario) {
        log.info("Consulta de examen realizado: id={}", id);
        Optional<ExamenRealizado> examenRealizado = service.obtenerPorId(id);

        if (examenRealizado.isEmpty()) {
            log.warn("El id de examen compartido no es valido: id={}", id);
            model.addAttribute("mensajeError", "Examen no encontrado");
            return "realizados/detalles";
        }

        ExamenRealizado realizado = examenRealizado.get();
        List<Pregunta> preguntas = preguntaService.obtenerPreguntasPorIdExamen(realizado.getExamen().getId());
        List<RespuestaDada> respuestaDadas = respuestaDadaService.obtenerRespuestasPorPreguntasYExamenRealizado(preguntas, realizado);
        model.addAttribute("respuestas", respuestaDadas);
        model.addAttribute("examen", realizado.getExamen());
//        model.addAttribute();
        return "realizados/detalles";
    }

    @GetMapping("/filtrar")
    public String filtrar(@RequestParam(value = "tipo", required = false) TipoExamen tipoExamen, @RequestParam(value = "nombre", required = false) String nombre, @AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal, Model model) {
        log.info("Realizando filtrado de los examenes realizados, parametros recibidos: tipo={}, nombre={}", tipoExamen, nombre);
        Usuario usuario = usuarioPrincipal.getUsuario();
        List<Examen> examenesDoctor;

        if(Objects.isNull(tipoExamen)) {
            examenesDoctor = examenService.obtenerPorIdCreadoPor(usuario);
        } else {
            examenesDoctor = examenService.obtenerPorCreadorYTipo(usuario, tipoExamen);
        }

        List<ExamenRealizado> examenesRealizados = service.obtenerExamenesRealizadosPorListaId(examenesDoctor);
        model.addAttribute("tipos", TipoExamen.values());

        if(Objects.nonNull(nombre)) {
            examenesRealizados = examenesRealizados.stream().filter(examen -> examen.getNombreUsuario().contains(nombre)).collect(Collectors.toList());
        }

        if (examenesRealizados.isEmpty()) {
           model.addAttribute("mensaje", "No se han encontrado resultados");
           return "realizados/index";
        }

        model.addAttribute("examenesRealizados", examenesRealizados);
        return "realizados/index";
    }

}
