package io.github.blackfishlabs.forza.data.customer;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmRepository;
import io.github.blackfishlabs.forza.domain.entity.CustomerEntity;
import io.github.blackfishlabs.forza.domain.pojo.Customer;

public class CustomerRealmRepository extends RealmRepository<Customer, CustomerEntity> implements CustomerRepository {

    public CustomerRealmRepository() {
        super(CustomerEntity.class, LocalDataInjector.customerMapper());
    }
}
