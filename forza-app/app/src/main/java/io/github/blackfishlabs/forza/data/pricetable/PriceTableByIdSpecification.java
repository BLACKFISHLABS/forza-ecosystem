package io.github.blackfishlabs.forza.data.pricetable;

import io.github.blackfishlabs.forza.data.realm.RealmSingleSpecification;
import io.github.blackfishlabs.forza.domain.entity.PriceTableEntity;
import io.realm.Realm;

public class PriceTableByIdSpecification implements RealmSingleSpecification<PriceTableEntity> {

    private final int priceTableId;

    public PriceTableByIdSpecification(final int priceTableId) {
        this.priceTableId = priceTableId;
    }

    @Override
    public PriceTableEntity toSingle(final Realm realm) {
        return realm.where(PriceTableEntity.class)
                .equalTo(PriceTableEntity.Fields.PRICE_TABLE_ID, priceTableId)
                .findFirst();
    }
}
