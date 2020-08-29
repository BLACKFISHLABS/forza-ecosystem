package io.github.blackfishlabs.forza.data.customer;

import io.github.blackfishlabs.forza.data.realm.RealmSingleSpecification;
import io.github.blackfishlabs.forza.domain.entity.CustomerEntity;
import io.realm.Realm;

public class CustomerByCustomerIdSpecification implements RealmSingleSpecification<CustomerEntity> {

    private final int customerId;

    public CustomerByCustomerIdSpecification(final int customerId) {
        this.customerId = customerId;
    }

    @Override
    public CustomerEntity toSingle(final Realm realm) {
        return realm.where(CustomerEntity.class).equalTo(CustomerEntity.Fields.CUSTOMER_ID, customerId).findFirst();
    }
}
