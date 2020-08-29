package io.github.blackfishlabs.forza.customer.model;

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
@Table(name = "APP_ROUTE")
@EqualsAndHashCode(callSuper = true)
public class RouteEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "description", nullable = false)
    private String name;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "last_change", nullable = false)
    private Date lastChangeTime;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity owner;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RouteCustomerEntity> customers = Lists.newArrayList();

    public void addItem(RouteCustomerEntity item) {
        item.setLastChangeTime(new Date());
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
