package io.github.blackfishlabs.forza.salesman.model;

import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "APP_VEHICLE")
@EqualsAndHashCode(callSuper = true)
public class VehicleEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "plate", nullable = false)
    private String plate;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "salesman_id")
    private SalesmanEntity salesman;
}
