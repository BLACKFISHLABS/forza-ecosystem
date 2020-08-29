package io.github.blackfishlabs.forza.core.application.company.model;

import lombok.Getter;

@Getter
public enum CompanyType {

    PARENT("Matriz"), COMPANY("Empresa");

    private String label;

    CompanyType(String label) {
        this.label = label;
    }
}
