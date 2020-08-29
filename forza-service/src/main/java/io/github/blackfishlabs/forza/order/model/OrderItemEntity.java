package io.github.blackfishlabs.forza.order.model;

import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "APP_ORDER_ITEM")
@EqualsAndHashCode(callSuper = true)
public class OrderItemEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private float quantity;

    @NotNull
    @Column(name = "sub_total", nullable = false)
    private double subTotal;

    @NotNull
    @Column(name = "sales_price", nullable = false)
    private double salesPrice;

    @Column(name = "last_change", nullable = false)
    private Date lastChangeTime;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "order_code")
    private String orderCode;

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("id", this.getId())
                .append("new", this.isNew())
                .toString();
    }
}
