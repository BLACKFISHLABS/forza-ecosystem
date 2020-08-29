package io.github.blackfishlabs.forza.data.company.pricetable;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmRepository;
import io.github.blackfishlabs.forza.domain.entity.CompanyPriceTableEntity;
import io.github.blackfishlabs.forza.domain.pojo.CompanyPriceTable;

public class CompanyPriceTableRealmRepository extends RealmRepository<CompanyPriceTable, CompanyPriceTableEntity> implements CompanyPriceTableRepository {

    public CompanyPriceTableRealmRepository() {
        super(CompanyPriceTableEntity.class, LocalDataInjector.companyPriceTableMapper());
    }
}
