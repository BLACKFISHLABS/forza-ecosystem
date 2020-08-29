package io.github.blackfishlabs.forza.data.paymentmethod;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmRepository;
import io.github.blackfishlabs.forza.domain.entity.PaymentMethodEntity;
import io.github.blackfishlabs.forza.domain.pojo.PaymentMethod;

public class PaymentMethodRealmRepository extends RealmRepository<PaymentMethod, PaymentMethodEntity> implements PaymentMethodRepository {

    public PaymentMethodRealmRepository() {
        super(PaymentMethodEntity.class, LocalDataInjector.paymentMethodMapper());
    }
}
