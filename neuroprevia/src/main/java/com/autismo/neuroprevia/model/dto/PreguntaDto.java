package com.autismo.neuroprevia.model.dto;

import com.autismo.neuroprevia.model.Pregunta;
import com.autismo.neuroprevia.model.RespuestaPosible;
import com.autismo.neuroprevia.model.enumeration.TipoRespuesta;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PreguntaDto {
    private Integer id;
    private String texto;
    private TipoRespuesta tipoRespuesta;
    @Builder.Default
    private int orden = 1;
    private List<RespuestaPosible> respuestaPosibles;

    public static PreguntaDto fromEntity(Pregunta p) {
        return PreguntaDto.builder()
                .id(p.getId())
                .texto(p.getTexto())
                .tipoRespuesta(TipoRespuesta.valueOf(p.getTipoRespuesta()))
                .orden(p.getOrden())
                .build();
    }

}
