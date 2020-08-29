package io.github.blackfishlabs.forza.core.application.phone.model;

import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "APP_PHONE")
@EqualsAndHashCode(callSuper = true)
public class PhoneEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type")
    private PhoneType phoneType;
}
