package io.github.blackfishlabs.forza.data.company.paymentmethod;

import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.CompanyPaymentMethodEntity;
import io.github.blackfishlabs.forza.domain.pojo.CompanyPaymentMethod;

public class CompanyPaymentMethodRealmMapper extends RealmMapper<CompanyPaymentMethod, CompanyPaymentMethodEntity> {

    @Override
    public CompanyPaymentMethodEntity toEntity(final CompanyPaymentMethod object) {
        return new CompanyPaymentMethodEntity()
                .withId(object.getCompanyId(), object.getPaymentMethodId());
    }

    @Override
    public CompanyPaymentMethod toViewObject(final CompanyPaymentMethodEntity entity) {
        return CompanyPaymentMethod
                .of(entity.getId(), entity.getCompanyId(), entity.getPaymentMethodId());
    }
}
