package io.github.blackfishlabs.forza.data.pricetable;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmRepository;
import io.github.blackfishlabs.forza.domain.entity.PriceTableEntity;
import io.github.blackfishlabs.forza.domain.pojo.PriceTable;

public class PriceTableRealmRepository extends RealmRepository<PriceTable, PriceTableEntity> implements PriceTableRepository {

    public PriceTableRealmRepository() {
        super(PriceTableEntity.class, LocalDataInjector.priceTableMapper());
    }
}
