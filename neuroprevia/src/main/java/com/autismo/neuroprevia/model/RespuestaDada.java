package com.autismo.neuroprevia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "respuestas_dadas")
public class RespuestaDada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_examen_realizado", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_respuestadada_examenrealizado"))
    private ExamenRealizado examenRealizado;

    @ManyToOne
    @JoinColumn(name = "id_pregunta", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_respuestadada_pregunta"))
    private Pregunta pregunta;

    @ManyToOne
    @JoinColumn(name = "id_respuesta", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_respuestadada_respuesta"))
    private RespuestaPosible respuesta;


    public ExamenRealizado getExamenRealizado() {
        return examenRealizado;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public RespuestaPosible getRespuesta() {
        return respuesta;
    }

    public void setExamenRealizado(ExamenRealizado examenRealizado) {
        this.examenRealizado = examenRealizado;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public void setRespuesta(RespuestaPosible respuesta) {
        this.respuesta = respuesta;
    }

}
