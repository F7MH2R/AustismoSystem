package com.autismo.neuroprevia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_examen", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_pregunta_examen"))
    private Examen examen;

    private String texto;
    private String tipoRespuesta;
    private int orden;
}


