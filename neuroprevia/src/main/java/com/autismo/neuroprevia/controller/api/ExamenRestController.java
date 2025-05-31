package com.autismo.neuroprevia.controller.api;

import com.autismo.neuroprevia.model.Examen;
import com.autismo.neuroprevia.model.ExamenRealizado;
import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.RespuestaDada;
import com.autismo.neuroprevia.model.RespuestaPosible;
import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.model.UsuarioPrincipal;
import com.autismo.neuroprevia.model.dto.PreguntaDto;
import com.autismo.neuroprevia.model.dto.ResultadoForm;
import com.autismo.neuroprevia.repository.examenRealizadoRepository;
import com.autismo.neuroprevia.repository.respuestaDadaRepository;
import com.autismo.neuroprevia.service.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.autismo.neuroprevia.model.enumeration.TipoRespuesta;



import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/examen")
public class ExamenRestController {

    private final PreguntaService preguntaService;
    private final examenRealizadoRepository examenRealizadoRepo;
    private final respuestaDadaRepository respuestaDadaRepo;

    @Autowired
    public ExamenRestController(PreguntaService preguntaService,
                                examenRealizadoRepository examenRealizadoRepo,
                                respuestaDadaRepository respuestaDadaRepo) {
        this.preguntaService     = preguntaService;
        this.examenRealizadoRepo = examenRealizadoRepo;
        this.respuestaDadaRepo   = respuestaDadaRepo;
    }

    @GetMapping("/{id}/preguntas")
    public List<PreguntaDto> preguntas(@PathVariable Integer id) {
        return preguntaService
                .listarPorExamen(id)              // List<Pregunta>
                .stream()
                .map(p -> PreguntaDto.builder()
                        .texto(p.getTexto())
                        .tipoRespuesta(TipoRespuesta.valueOf(p.getTipoRespuesta()))
                        .orden(p.getOrden())
                        .build()
                )
                .toList();
    }

    @PostMapping(path = "/{id}/submit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> submitExamen(
            @PathVariable Integer id,
            @RequestBody ResultadoForm form,
            @AuthenticationPrincipal UsuarioPrincipal principal) {

        // 1) Crear y guardar ExamenRealizado
        Usuario usuario = principal.getUsuario();
        ExamenRealizado er = new ExamenRealizado();
        Examen examen = new Examen();
        examen.setId(id);
        er.setExamen(examen);
        er.setUsuario(usuario);
        er.setFechaRealizacion(Timestamp.from(Instant.now()));
        er = examenRealizadoRepo.save(er);

        // 2) Guardar cada RespuestaDada
        for (ResultadoForm.Respuesta r : form.getRespuestas()) {
            RespuestaDada rd = new RespuestaDada();
            rd.setExamenRealizado(er);

            Pregunta p = new Pregunta();
            p.setId(r.getPreguntaId());
            rd.setPregunta(p);

            RespuestaPosible rp = new RespuestaPosible();
            rp.setId(Integer.parseInt(r.getRespuesta()));
            rd.setRespuesta(rp);

            respuestaDadaRepo.save(rd);
        }

        // 3) Calcular puntaje e interpretación
        float score = form.getRespuestas().stream()
                .mapToInt(r -> Integer.parseInt(r.getRespuesta()))
                .sum();
        er.setResultadoTotal(score);
        er.setInterpretacion(score < 10 ? "Bajo" : score < 20 ? "Medio" : "Alto");
        examenRealizadoRepo.save(er);

        return ResponseEntity.ok().build();
    }
}
