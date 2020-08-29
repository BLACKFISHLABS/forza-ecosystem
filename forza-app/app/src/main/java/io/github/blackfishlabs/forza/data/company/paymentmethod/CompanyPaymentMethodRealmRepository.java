package io.github.blackfishlabs.forza.data.company.paymentmethod;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmRepository;
import io.github.blackfishlabs.forza.domain.entity.CompanyPaymentMethodEntity;
import io.github.blackfishlabs.forza.domain.pojo.CompanyPaymentMethod;

public class CompanyPaymentMethodRealmRepository extends RealmRepository<CompanyPaymentMethod, CompanyPaymentMethodEntity> implements CompanyPaymentMethodRepository {

    public CompanyPaymentMethodRealmRepository() {
        super(CompanyPaymentMethodEntity.class, LocalDataInjector.companyPaymentMethodMapper());
    }
}
