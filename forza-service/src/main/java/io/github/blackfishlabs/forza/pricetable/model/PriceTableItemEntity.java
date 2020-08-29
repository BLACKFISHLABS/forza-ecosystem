package io.github.blackfishlabs.forza.pricetable.model;

import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import io.github.blackfishlabs.forza.product.model.ProductEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "APP_PRICE_TABLE_ITEM")
@EqualsAndHashCode(callSuper = true)
public class PriceTableItemEntity extends BasicEntity<Long> {

    @NotNull
    @Column(name = "price", nullable = false)
    private Double salesPrice;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Float stockQuantity;

    @Column(name = "last_change", nullable = false)
    private Date lastChangeTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pricetable_id")
    private PriceTableEntity owner;
}
