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
@Table(name = "APP_CHARGE_TABLE_PRICE")
@EqualsAndHashCode(callSuper = true)
public class ChargeTablePriceEntity extends BasicEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "charge_block_id")
    private ChargeBlockEntity owner;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargeItemEntity> items = Lists.newArrayList();

    public void addItem(ChargeItemEntity item) {
        item.setOwner(this);
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
