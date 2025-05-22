package com.autismo.neuroprevia.model;

import jakarta.persistence.*;

@Entity
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
}
