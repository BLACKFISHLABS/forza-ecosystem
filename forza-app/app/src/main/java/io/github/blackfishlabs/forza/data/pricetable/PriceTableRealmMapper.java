package io.github.blackfishlabs.forza.data.pricetable;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.PriceTableEntity;
import io.github.blackfishlabs.forza.domain.entity.PriceTableItemEntity;
import io.github.blackfishlabs.forza.domain.pojo.PriceTable;
import io.realm.RealmList;

public class PriceTableRealmMapper extends RealmMapper<PriceTable, PriceTableEntity> {

    @Override
    public PriceTableEntity toEntity(final PriceTable object) {
        return new PriceTableEntity()
                .withPriceTableId(object.getPriceTableId())
                .withCode(object.getCode())
                .withName(object.getName())
                .withItems((RealmList<PriceTableItemEntity>) LocalDataInjector.priceTableItemMapper().toEntities(object.getItems()))
                .withActive(object.isActive())
                .withLastChangeTime(object.getLastChangeTime());
    }

    @Override
    public PriceTable toViewObject(final PriceTableEntity entity) {
        return new PriceTable()
                .withPriceTableId(entity.getPriceTableId())
                .withCode(entity.getCode())
                .withName(entity.getName())
                .withItems(LocalDataInjector.priceTableItemMapper().toViewObjects(entity.getItems()))
                .withActive(entity.isActive())
                .withLastChangeTime(entity.getLastChangeTime());
    }
}
