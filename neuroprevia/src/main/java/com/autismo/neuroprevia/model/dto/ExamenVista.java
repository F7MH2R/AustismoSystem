package com.autismo.neuroprevia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExamenVista {
    private Integer id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private int edadMinima;
    private int edadMaxima;
    private int numeroPreguntas;
    private int duracion;
}
