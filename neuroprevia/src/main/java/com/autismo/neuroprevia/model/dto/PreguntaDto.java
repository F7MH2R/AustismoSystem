package com.autismo.neuroprevia.model.dto;

import com.autismo.neuroprevia.model.enumeration.TipoRespuesta;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PreguntaDto {
    private String texto;
    private TipoRespuesta tipoRespuesta;
    @Builder.Default
    private int orden = 1;
}
