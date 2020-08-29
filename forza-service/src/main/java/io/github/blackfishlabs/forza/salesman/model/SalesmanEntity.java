package io.github.blackfishlabs.forza.salesman.model;

import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "APP_SALESMAN")
@EqualsAndHashCode(callSuper = true)
public class SalesmanEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "document", nullable = false)
    private String cpfOrCnpj;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phoneNumber;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "last_change", nullable = false)
    private Date lastChangeTime;

    @Column(name = "apply_discount")
    private Boolean canApplyDiscount;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "APP_SALESMAN_COMPANY", joinColumns = @JoinColumn(name = "salesman_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
    private List<CompanyEntity> companies;
}
