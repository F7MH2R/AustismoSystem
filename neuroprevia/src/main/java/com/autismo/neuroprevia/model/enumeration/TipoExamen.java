package com.autismo.neuroprevia.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum TipoExamen {
    ADULTO("Adulto");

    private final String value;
}
