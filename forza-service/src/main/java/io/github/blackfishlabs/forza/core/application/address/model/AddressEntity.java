package io.github.blackfishlabs.forza.core.application.address.model;

import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "APP_ADDRESS")
@EqualsAndHashCode(callSuper = true)
public class AddressEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "street", nullable = false)
    private String street;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "location")
    private String location;

    @NotNull
    @Column(name = "neighborhood", nullable = false)
    private String neighborhood;

    @Column(name = "complement")
    private String complement;

    @NotNull
    @Column(name = "cep", nullable = false)
    private String cep;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private CityEntity city;
}

