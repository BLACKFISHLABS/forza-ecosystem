package io.github.blackfishlabs.forza.charge.model;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "APP_CHARGE_BLOCK")
@EqualsAndHashCode(callSuper = true)
public class ChargeBlockEntity extends BasicEntity<Long> {

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargeTablePriceEntity> tables = Lists.newArrayList();

    public void addItem(ChargeTablePriceEntity item) {
        item.setOwner(this);
        getTables().add(item);
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("id", this.getId())
                .append("new", this.isNew())
                .toString();
    }
}
