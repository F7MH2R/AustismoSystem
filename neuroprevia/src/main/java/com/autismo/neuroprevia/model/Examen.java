package com.autismo.neuroprevia.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;
@Entity
@Table(name = "examenes")
public class Examen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titulo;
    private String descripcion;
    private String tipo;

    private int edadMinima;
    private int edadMaxima;

    @ManyToOne
    @JoinColumn(name = "creado_por", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_examen_usuario"))
    private Usuario creadoPor;

    private Timestamp fechaCreacion = Timestamp.from(Instant.now());
}
