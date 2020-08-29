package io.github.blackfishlabs.forza.charge.model;

import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "APP_CHARGE_CUSTOMER")
@EqualsAndHashCode(callSuper = true)
public class ChargeCustomerEntity extends BasicEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private ChargeRouteEntity owner;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "fantasy_name", nullable = false)
    private String fantasyName;

    @NotNull
    @Column(name = "document", nullable = false)
    private String document;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "neighborhood", nullable = false)
    private String neighborhood;

    @NotNull
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "complement")
    private String complement;

    @Column(name = "contact")
    private String contact;

    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "secondary_phone")
    private String secondaryPhone;

    @NotNull
    @Column(name = "type_person", nullable = false)
    private Integer typePerson;
}
