package com.autismo.neuroprevia.model.dto;

import com.autismo.neuroprevia.model.enumeration.TipoExamen;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExamenDto {
    private String descripcion;
    @Builder.Default
    private int edadMaxima = 1;
    @Builder.Default
    private int edadMinima = 1;
    private TipoExamen tipo;
    private String titulo;
}
