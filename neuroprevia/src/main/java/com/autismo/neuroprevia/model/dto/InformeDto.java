package com.autismo.neuroprevia.model.dto;

import java.time.LocalDateTime;

public class InformeDto {
    private Long id;
    private String nombrePaciente;
    private String examenTitulo;
    private LocalDateTime fechaRealizacion;
    private Double resultadoTotal;
    private String interpretacion;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getExamenTitulo() { return examenTitulo; }
    public void setExamenTitulo(String examenTitulo) { this.examenTitulo = examenTitulo; }

    public LocalDateTime getFechaRealizacion() { return fechaRealizacion; }
    public void setFechaRealizacion(LocalDateTime fechaRealizacion) { this.fechaRealizacion = fechaRealizacion; }

    public Double getResultadoTotal() { return resultadoTotal; }
    public void setResultadoTotal(Double resultadoTotal) { this.resultadoTotal = resultadoTotal; }

    public String getInterpretacion() { return interpretacion; }
    public void setInterpretacion(String interpretacion) { this.interpretacion = interpretacion; }
}
