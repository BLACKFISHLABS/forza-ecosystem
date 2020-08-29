package io.github.blackfishlabs.forza.charge.model;

import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "APP_CHARGE_PRODUCT")
@EqualsAndHashCode(callSuper = true)
public class ChargeProductEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "ean", nullable = false)
    private String ean;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "group_pro", nullable = false)
    private String group;

    @NotNull
    @Column(name = "note", nullable = false)
    private String note;

    @NotNull
    @Column(name = "qnt", nullable = false)
    private float qnt;

    @NotNull
    @Column(name = "unit", nullable = false)
    private String unit;
}
