package io.github.blackfishlabs.forza.customer.model;

import io.github.blackfishlabs.forza.core.application.address.model.CityEntity;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.application.enumeration.TypeOfPerson;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "APP_CUSTOMER")
@EqualsAndHashCode(callSuper = true)
public class CustomerEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "fantasy_name", nullable = false)
    private String fantasyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "person_type", nullable = false)
    private TypeOfPerson typeOfPerson;

    @NotNull
    @Column(name = "document", nullable = false)
    private String cpfOrCnpj;

    @Column(name = "contact")
    private String contact;

    @Column(name = "email")
    private String email;

    @Column(name = "main_phone")
    private String mainPhone;

    @Column(name = "secondary_phone")
    private String secondaryPhone;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "district")
    private String district;

    @Column(name = "address_number")
    private String addressNumber;

    @Column(name = "address_complement")
    private String addressComplement;

    @Column(name = "pricetable_id")
    private Integer priceTableId;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private CityEntity city;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "last_change")
    private Date lastChangeTime;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity owner;

    @Column(name = "ie")
    private String ie;

    @Column(name = "buyer")
    private String buyer;
}
