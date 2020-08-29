package io.github.blackfishlabs.forza.data.company.pricetable;

import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.CompanyPriceTableEntity;
import io.github.blackfishlabs.forza.domain.pojo.CompanyPriceTable;

public class CompanyPriceTableRealmMapper extends RealmMapper<CompanyPriceTable, CompanyPriceTableEntity> {

    @Override
    public CompanyPriceTableEntity toEntity(final CompanyPriceTable object) {
        return new CompanyPriceTableEntity()
                .withId(object.getCompanyId(), object.getPriceTableId());
    }

    @Override
    public CompanyPriceTable toViewObject(final CompanyPriceTableEntity entity) {
        return CompanyPriceTable
                .of(entity.getId(), entity.getCompanyId(), entity.getPriceTableId());
    }
}
