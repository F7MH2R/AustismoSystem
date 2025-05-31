package com.autismo.neuroprevia.model.dto;

import java.util.List;

public class ResultadoForm {

    public static class Respuesta {
        private Integer preguntaId;
        private String respuesta;

        public Integer getPreguntaId() {
            return preguntaId;
        }
        public void setPreguntaId(Integer preguntaId) {
            this.preguntaId = preguntaId;
        }

        public String getRespuesta() {
            return respuesta;
        }
        public void setRespuesta(String respuesta) {
            this.respuesta = respuesta;
        }
    }

    private List<Respuesta> respuestas;

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }
    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}
