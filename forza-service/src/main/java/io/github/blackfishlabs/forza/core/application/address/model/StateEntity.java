package io.github.blackfishlabs.forza.core.application.address.model;

import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "APP_STATE")
@EqualsAndHashCode(callSuper = true)
public class StateEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "code", nullable = false)
    private Integer code;

    @NotNull
    @Column(name = "state", nullable = false)
    private String state;

    @NotNull
    @Column(name = "acronym", nullable = false)
    private String acronym;
}
