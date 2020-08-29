package io.github.blackfishlabs.forza.core.application.company.model;

import io.github.blackfishlabs.forza.core.application.address.model.AddressEntity;
import io.github.blackfishlabs.forza.core.application.contact.model.ContactEntity;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "APP_COMPANY")
@EqualsAndHashCode(callSuper = true)
public class CompanyEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "fantasy_name", nullable = false)
    private String fantasyName;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotNull
    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private ContactEntity contact;

    @Column(name = "company_type")
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @Column(name = "pricetable_id")
    private Integer priceTableId;
}

