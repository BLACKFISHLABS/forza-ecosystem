package io.github.blackfishlabs.forza.customer.model;

import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "APP_ROUTE_CUSTOMER")
@EqualsAndHashCode(callSuper = true)
public class RouteCustomerEntity extends BasicEntity<Long> {

    @Column(name = "last_change", nullable = false)
    private Date lastChangeTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private RouteEntity owner;
}
