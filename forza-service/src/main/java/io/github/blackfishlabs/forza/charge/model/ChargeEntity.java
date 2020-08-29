package io.github.blackfishlabs.forza.charge.model;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "APP_CHARGE")
@EqualsAndHashCode(callSuper = true)
public class ChargeEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "issue_date", nullable = false)
    private Date issueDate;

    @NotNull
    @Column(name = "salesman", nullable = false)
    private String salesmanCode;

    @NotNull
    @Column(name = "emitter", nullable = false)
    private String emitterCode;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "charge_block_id")
    private ChargeBlockEntity chargeBlock;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id")
    private ChargeVehicleEntity vehicle;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChargeStatus status = ChargeStatus.STATUS_CREATED;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargeRouteEntity> routes = Lists.newArrayList();

    public void addItem(ChargeRouteEntity item) {
        item.setOwner(this);
        getRoutes().add(item);
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("id", this.getId())
                .append("new", this.isNew())
                .toString();
    }

}
