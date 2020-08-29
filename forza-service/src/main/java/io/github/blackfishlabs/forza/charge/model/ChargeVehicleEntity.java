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
@Table(name = "APP_CHARGE_VEHICLE")
@EqualsAndHashCode(callSuper = true)
public class ChargeVehicleEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "plate", nullable = false)
    private String plate;

    @NotNull
    @Column(name = "init_km", nullable = false)
    private Integer initKm;

    @Column(name = "end_km")
    private Integer endKm;
}
