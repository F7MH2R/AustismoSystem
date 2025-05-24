package com.autismo.neuroprevia.model.dto;

import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.RespuestaPosible;

public class RespuestaDetalleDto {

    private Pregunta pregunta;
    private RespuestaPosible respuesta;

    public RespuestaDetalleDto() {}

    public RespuestaDetalleDto(Pregunta pregunta, RespuestaPosible respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
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
}
