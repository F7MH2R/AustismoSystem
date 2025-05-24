package com.autismo.neuroprevia.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RespuestaPosibleDto {
    private String textoRespuesta;
    private int valorNumerico;
}
