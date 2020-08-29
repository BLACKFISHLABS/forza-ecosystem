package io.github.blackfishlabs.forza.core.application.contact.model;

import io.github.blackfishlabs.forza.core.application.phone.model.PhoneEntity;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "APP_CONTACT")
@EqualsAndHashCode(callSuper = true)
public class ContactEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "phone_id")
    private PhoneEntity phone;
}
