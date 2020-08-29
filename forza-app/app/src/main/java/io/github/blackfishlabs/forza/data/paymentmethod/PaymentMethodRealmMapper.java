package io.github.blackfishlabs.forza.data.paymentmethod;

import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.PaymentMethodEntity;
import io.github.blackfishlabs.forza.domain.pojo.PaymentMethod;

public class PaymentMethodRealmMapper extends RealmMapper<PaymentMethod, PaymentMethodEntity> {

    @Override
    public PaymentMethodEntity toEntity(final PaymentMethod object) {
        return new PaymentMethodEntity()
                .withPaymentMethodId(object.getPaymentMethodId())
                .withCode(object.getCode())
                .withDescription(object.getDescription())
                .withDiscountPercentage(object.getDiscountPercentage())
                .withCompanyId(object.getCompanyId())
                .withActive(object.isActive())
                .withLastChangeTime(object.getLastChangeTime());
    }

    @Override
    public PaymentMethod toViewObject(final PaymentMethodEntity entity) {
        return new PaymentMethod()
                .withPaymentMethodId(entity.getPaymentMethodId())
                .withCode(entity.getCode())
                .withDescription(entity.getDescription())
                .withDiscountPercentage(entity.getDiscountPercentage())
                .withCompanyId(entity.getCompanyId())
                .withActive(entity.isActive())
                .withLastChangeTime(entity.getLastChangeTime());
    }
}
