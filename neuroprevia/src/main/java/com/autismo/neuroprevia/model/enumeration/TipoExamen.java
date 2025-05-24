package com.autismo.neuroprevia.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum TipoExamen {
    INFANTIL("Infantil"),
    ADULTO("Adulto"),
    GENERAL("General");

    private final String value;
}
