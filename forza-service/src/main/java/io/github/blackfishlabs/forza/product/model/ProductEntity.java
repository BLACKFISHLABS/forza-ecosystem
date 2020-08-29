package io.github.blackfishlabs.forza.product.model;

import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "APP_PRODUCT")
@EqualsAndHashCode(callSuper = true)
public class ProductEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "ean", nullable = false)
    private String barCode;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "group_id")
    private String group;

    @Column(name = "note")
    private String observation;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "last_change", nullable = false)
    private Date lastChangeTime;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity owner;
}
