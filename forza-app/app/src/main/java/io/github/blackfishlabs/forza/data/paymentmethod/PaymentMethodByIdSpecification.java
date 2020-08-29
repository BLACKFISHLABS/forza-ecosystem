package io.github.blackfishlabs.forza.data.paymentmethod;

import io.github.blackfishlabs.forza.data.realm.RealmSingleSpecification;
import io.github.blackfishlabs.forza.domain.entity.PaymentMethodEntity;
import io.realm.Realm;

public class PaymentMethodByIdSpecification implements RealmSingleSpecification<PaymentMethodEntity> {

    private final int paymentMethodId;

    public PaymentMethodByIdSpecification(final int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    @Override
    public PaymentMethodEntity toSingle(final Realm realm) {
        return realm.where(PaymentMethodEntity.class)
                .equalTo(PaymentMethodEntity.Fields.PAYMENT_METHOD_ID, paymentMethodId)
                .findFirst();
    }
}
