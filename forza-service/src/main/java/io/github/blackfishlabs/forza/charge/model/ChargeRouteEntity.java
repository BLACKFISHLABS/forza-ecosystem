package io.github.blackfishlabs.forza.charge.model;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "APP_CHARGE_ROUTE")
@EqualsAndHashCode(callSuper = true)
public class ChargeRouteEntity extends BasicEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "charge_id")
    private ChargeEntity owner;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargeCustomerEntity> customers = Lists.newArrayList();

    public void addItem(ChargeCustomerEntity item) {
        item.setOwner(this);
        getCustomers().add(item);
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("id", this.getId())
                .append("new", this.isNew())
                .toString();
    }
}
