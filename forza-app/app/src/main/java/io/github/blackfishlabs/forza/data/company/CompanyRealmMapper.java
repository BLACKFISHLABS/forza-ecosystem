package io.github.blackfishlabs.forza.data.company;

import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.CompanyEntity;
import io.github.blackfishlabs.forza.domain.pojo.Company;

public class CompanyRealmMapper extends RealmMapper<Company, CompanyEntity> {

    @Override
    public CompanyEntity toEntity(final Company object) {
        return new CompanyEntity()
                .withCompanyId(object.getCompanyId())
                .withName(object.getName())
                .withCnpj(object.getCnpj())
                .withPriceTableId(object.getPriceTableId());
    }

    @Override
    public Company toViewObject(final CompanyEntity entity) {
        return new Company()
                .withCompanyId(entity.getCompanyId())
                .withName(entity.getName())
                .withCnpj(entity.getCnpj())
                .withPriceTableId(entity.getPriceTableId());
    }
}
