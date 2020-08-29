package io.github.blackfishlabs.forza.core.application.address.model;

import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "APP_CITY")
@EqualsAndHashCode(callSuper = true)
public class CityEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "code", nullable = false)
    private Integer code;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    private StateEntity state;
}