package com.autismo.neuroprevia.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TipoRespuesta {
    CERRADA("Cerrada"),
    SELECCION_UNICA("Selección Única"),
    OPCION_MULTIPLE("Opción Múltiple"),
    TEXTO_LIBRE("Texto Libre"),
    ESCALA_1_5("Escala 1-5");

    private final String value;

    public static TipoRespuesta fromValue(String v) {
        return Arrays.stream(values())
                .filter(tp -> tp.getValue().equalsIgnoreCase(v))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "TipoRespuesta desconocido: " + v));
    }
}
