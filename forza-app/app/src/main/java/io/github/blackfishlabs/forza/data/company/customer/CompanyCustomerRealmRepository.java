package io.github.blackfishlabs.forza.data.company.customer;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmRepository;
import io.github.blackfishlabs.forza.domain.entity.CompanyCustomerEntity;
import io.github.blackfishlabs.forza.domain.pojo.CompanyCustomer;

public class CompanyCustomerRealmRepository extends RealmRepository<CompanyCustomer, CompanyCustomerEntity> implements CompanyCustomerRepository {

    public CompanyCustomerRealmRepository() {
        super(CompanyCustomerEntity.class, LocalDataInjector.companyCustomerMapper());
    }
}
