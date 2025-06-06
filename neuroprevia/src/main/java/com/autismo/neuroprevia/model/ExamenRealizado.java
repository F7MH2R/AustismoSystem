package com.autismo.neuroprevia.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "examenes_realizados")
public class ExamenRealizado {
    private final String NOMBRE_COMPLETO = "%s %s";

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
    public float getResultadoTotal() { return resultadoTotal; }
    public void setResultadoTotal(float resultadoTotal) { this.resultadoTotal = resultadoTotal; }


    @Column(columnDefinition = "TEXT")
    private String interpretacion;

    public int getId() {
        return id;
    }

    public Examen getExamen() {
        return examen;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Timestamp getFechaRealizacion() {
        return fechaRealizacion;
    }

    public String getInterpretacion() {
        return interpretacion;
    }

    public void setInterpretacion(String interpretacion) {
        this.interpretacion = interpretacion;
    }

    public String getNombreUsuario() {
        return String.format(NOMBRE_COMPLETO, this.usuario.getNombre(), this.usuario.getApellido()).trim();
    }

}
