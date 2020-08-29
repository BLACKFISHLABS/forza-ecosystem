package io.github.blackfishlabs.forza.charge.model;

import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "APP_CHARGE_ITEM")
@EqualsAndHashCode(callSuper = true)
public class ChargeItemEntity extends BasicEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_table_price_id")
    private ChargeTablePriceEntity owner;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private ChargeProductEntity product;
}
