package io.github.blackfishlabs.forza.core.application.phone.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum PhoneType {

    PHONE("Fixo"),
    CELL_PHONE("Celular");

    private String label;

    PhoneType(String label) {
        this.label = label;
    }
}

