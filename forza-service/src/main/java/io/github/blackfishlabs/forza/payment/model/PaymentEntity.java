package io.github.blackfishlabs.forza.payment.model;

import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "APP_PAYMENT")
@EqualsAndHashCode(callSuper = true)
public class PaymentEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "discount_percentage", nullable = false)
    private Float discountPercentage;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "last_change", nullable = false)
    private Date lastChangeTime;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity owner;
}
