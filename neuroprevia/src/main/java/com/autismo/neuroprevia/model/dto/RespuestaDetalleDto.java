package com.autismo.neuroprevia.model.dto;

import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.RespuestaPosible;

import jakarta.persistence.Transient;
import java.util.List;

public class RespuestaDetalleDto {

    private Pregunta pregunta;
    private RespuestaPosible respuesta;

    private Integer preguntaId;
    private Integer respuestaIdSeleccionada;

    @Transient
    private List<RespuestaPosible> respuestas; // ✅ NUEVO

    public RespuestaDetalleDto() {}

    public RespuestaDetalleDto(Pregunta pregunta, RespuestaPosible respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.preguntaId = pregunta.getId();
        this.respuestaIdSeleccionada = respuesta.getId();
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public RespuestaPosible getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(RespuestaPosible respuesta) {
        this.respuesta = respuesta;
    }

    public Integer getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(Integer preguntaId) {
        this.preguntaId = preguntaId;
    }

    public Integer getRespuestaIdSeleccionada() {
        return respuestaIdSeleccionada;
    }

    public void setRespuestaIdSeleccionada(Integer respuestaIdSeleccionada) {
        this.respuestaIdSeleccionada = respuestaIdSeleccionada;
    }

    public List<RespuestaPosible> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<RespuestaPosible> respuestas) {
        this.respuestas = respuestas;
    }
}

