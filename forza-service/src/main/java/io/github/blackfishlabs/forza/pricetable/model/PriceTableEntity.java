package io.github.blackfishlabs.forza.pricetable.model;

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
@Table(name = "APP_PRICE_TABLE")
@EqualsAndHashCode(callSuper = true)
public class PriceTableEntity extends BasicEntity<Long> {

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
    private List<PriceTableItemEntity> items = Lists.newArrayList();

    public void addItem(PriceTableItemEntity item) {
        item.setLastChangeTime(new Date());
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
