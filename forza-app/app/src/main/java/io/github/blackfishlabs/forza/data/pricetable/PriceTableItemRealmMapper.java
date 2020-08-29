package io.github.blackfishlabs.forza.data.pricetable;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.PriceTableItemEntity;
import io.github.blackfishlabs.forza.domain.pojo.PriceTableItem;

public class PriceTableItemRealmMapper extends RealmMapper<PriceTableItem, PriceTableItemEntity> {

    @Override
    public PriceTableItemEntity toEntity(final PriceTableItem object) {
        return new PriceTableItemEntity()
                .withItemId(object.getItemId())
                .withSalesPrice(object.getSalesPrice())
                .withStockQuantity(object.getStockQuantity())
                .withProductId(object.getProductId())
                .withProduct(LocalDataInjector.productMapper().toEntity(object.getProduct()))
                .withLastChangeTime(object.getLastChangeTime());
    }

    @Override
    public PriceTableItem toViewObject(final PriceTableItemEntity entity) {
        return new PriceTableItem()
                .withItemId(entity.getItemId())
                .withSalesPrice(entity.getSalesPrice())
                .withStockQuantity(entity.getStockQuantity())
                .withProductId(entity.getProductId())
                .withProduct(LocalDataInjector.productMapper().toViewObject(entity.getProduct()))
                .withLastChangeTime(entity.getLastChangeTime());
    }
}
