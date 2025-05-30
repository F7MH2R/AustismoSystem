package com.autismo.neuroprevia.controller;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.ExamenRealizado;
import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.model.UsuarioPrincipal;
import com.autismo.neuroprevia.service.ExamenRealizadoService;
import com.autismo.neuroprevia.service.ExamenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/examenes/realizados")
@RequiredArgsConstructor
@Slf4j
public class ExamenRealizadoController {
    private final ExamenRealizadoService service;
    private final ExamenService examenService;

    @GetMapping()
    public String index(Model model, @AuthenticationPrincipal UsuarioPrincipal usuarioPrincipal) {
        Usuario usuario = usuarioPrincipal.getUsuario();
        List<Examen> examenesDoctor = examenService.obtenerPorIdCreadoPor(usuario);
        List<ExamenRealizado> examenesRealizados = service.obtenerExamenesRealizadosPorListaId(examenesDoctor);
        model.addAttribute("examenes", examenesDoctor);
        model.addAttribute("examenesRealizados", examenesRealizados);
        return "realizados/index";
    }

}
