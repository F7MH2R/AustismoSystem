package com.autismo.neuroprevia.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "examenes_realizados")
public class ExamenRealizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_examen", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_examenrealizado_examen"))
    private Examen examen;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_examenrealizado_usuario"))
    private Usuario usuario;

    private Timestamp fechaRealizacion = Timestamp.from(Instant.now());
    private float resultadoTotal;

    @Column(columnDefinition = "TEXT")
    private String interpretacion;
}
