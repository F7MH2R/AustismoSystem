package com.autismo.neuroprevia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "preguntas")
@Getter
@Setter
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

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RespuestaPosible> respuestaPosibles;

}


