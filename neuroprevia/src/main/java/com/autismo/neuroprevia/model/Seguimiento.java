package com.autismo.neuroprevia.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "seguimientos")
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int idDoctor;   // FK manual (Usuario con rol DOCTOR)
    private int idPaciente; // FK manual (Usuario con rol PACIENTE)

    private LocalDate fechaCita;

    @Column(columnDefinition = "TEXT")
    private String notas;

    private String estado; // pendiente, realizado, cancelado

    // Getters y setters
}

