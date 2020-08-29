package io.github.blackfishlabs.forza.data.order;

import io.github.blackfishlabs.forza.data.realm.RealmSingleSpecification;
import io.github.blackfishlabs.forza.domain.entity.OrderEntity;
import io.realm.Realm;

public class OrderByIdSpecification implements RealmSingleSpecification<OrderEntity> {

    private final int orderId;

    public OrderByIdSpecification(final int orderId) {
        this.orderId = orderId;
    }

    @Override
    public OrderEntity toSingle(final Realm realm) {
        return realm.where(OrderEntity.class)
                .equalTo(OrderEntity.Fields.ORDER_ID, orderId)
                .findFirst();
    }
}
