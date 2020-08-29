package io.github.blackfishlabs.forza.order.model;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import io.github.blackfishlabs.forza.salesman.model.SalesmanEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "APP_ORDER")
@EqualsAndHashCode(callSuper = true)
public class OrderEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "code", nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OrderType type;

    @Column(name = "issue_date")
    private Date issueDate;

    @Column(name = "discount")
    private double discount;

    @Column(name = "discount_percentage")
    private float discountPercentage;

    @Column(name = "note")
    private String observation;

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @NotNull
    @Column(name = "payment_id", nullable = false)
    private Long paymentMethodId;

    @NotNull
    @Column(name = "pricetable_id", nullable = false)
    private Long priceTableId;

    @Column(name = "last_change", nullable = false)
    private Date lastChangeTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesman_id")
    private SalesmanEntity salesman;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = Lists.newArrayList();

    @Column(name = "resume_code")
    private String resumeCode;

    @Column(name = "customer_code")
    private String customerCode;

    @Column(name = "payment_code")
    private String paymentCode;

    @Column(name = "total")
    private double total;

    public void addItem(OrderItemEntity item) {
        item.setLastChangeTime(new Date());
        item.setOrder(this);
        item.setOrderCode(String.valueOf(this.getOrderId()));

        getItems().add(item);
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("id", this.getId())
                .append("new", this.isNew())
                .toString();
    }
}
