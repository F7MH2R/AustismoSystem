package com.autismo.neuroprevia.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoRespuesta {
    ABIERTA("Abierta"),
    OPCION_MULTIPLE("Opcion multiple"),
    EXCLUYENTE("Excluyente"),
    CERRADA("Cerrada");

    private final String value;
}
