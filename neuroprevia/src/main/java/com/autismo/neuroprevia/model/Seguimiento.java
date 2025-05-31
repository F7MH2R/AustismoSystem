package com.autismo.neuroprevia.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "seguimientos")
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int idDoctor;

    private LocalDate fechaCita;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Usuario paciente;

    @Column(columnDefinition = "TEXT")
    private String notas;

    private String estado;

    // Getters y setters
    public int getId() { return id; }

    public int getIdDoctor() { return idDoctor; }
    public void setIdDoctor(int idDoctor) { this.idDoctor = idDoctor; }

    public LocalDate getFechaCita() { return fechaCita; }
    public void setFechaCita(LocalDate fechaCita) { this.fechaCita = fechaCita; }

    public Usuario getPaciente() { return paciente; }
    public void setPaciente(Usuario paciente) { this.paciente = paciente; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
