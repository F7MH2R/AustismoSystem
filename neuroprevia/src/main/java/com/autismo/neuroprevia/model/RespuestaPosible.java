package com.autismo.neuroprevia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "respuestas_posibles")
public class RespuestaPosible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_pregunta", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_respuesta_pregunta"))
    private Pregunta pregunta;

    private String textoRespuesta;
    private float valorNumerico;
}
