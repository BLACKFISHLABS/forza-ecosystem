package io.github.blackfishlabs.forza.data.company.customer;

import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.CompanyCustomerEntity;
import io.github.blackfishlabs.forza.domain.pojo.CompanyCustomer;

public class CompanyCustomerRealmMapper extends RealmMapper<CompanyCustomer, CompanyCustomerEntity> {

    @Override
    public CompanyCustomerEntity toEntity(final CompanyCustomer object) {
        return new CompanyCustomerEntity()
                .withId(object.getCompanyId(), object.getCustomerId());
    }

    @Override
    public CompanyCustomer toViewObject(final CompanyCustomerEntity entity) {
        return CompanyCustomer
                .of(entity.getId(), entity.getCompanyId(), entity.getCustomerId());
    }
}
