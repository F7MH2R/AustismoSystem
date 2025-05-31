package com.autismo.neuroprevia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "respuestas_posibles")
@Getter
@Setter
public class RespuestaPosible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String texto;

    @ManyToOne
    @JoinColumn(name = "id_pregunta", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_respuesta_pregunta"))
    private Pregunta pregunta;

    private String textoRespuesta;
    private float valorNumerico;
}
