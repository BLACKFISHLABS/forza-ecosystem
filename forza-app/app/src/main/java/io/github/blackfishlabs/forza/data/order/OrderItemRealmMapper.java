package io.github.blackfishlabs.forza.data.order;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.OrderItemEntity;
import io.github.blackfishlabs.forza.domain.pojo.OrderItem;

public class OrderItemRealmMapper extends RealmMapper<OrderItem, OrderItemEntity> {

    @Override
    public OrderItemEntity toEntity(final OrderItem object) {
        return new OrderItemEntity()
                .withId(object.getId())
                .withOrderItemId(object.getOrderItemId())
                .withItem(LocalDataInjector.priceTableItemMapper().toEntity(object.getItem()))
                .withSalesPrice(object.getSalesPrice())
                .withQuantity(object.getQuantity())
                .withSubTotal(object.getSubTotal())
                .withLastChangeTime(object.getLastChangeTime());
    }

    @Override
    public OrderItem toViewObject(final OrderItemEntity entity) {
        return new OrderItem()
                .withId(entity.getId())
                .withOrderItemId(entity.getOrderItemId())
                .withItem(LocalDataInjector.priceTableItemMapper().toViewObject(entity.getItem()))
                .withSalesPrice(entity.getSalesPrice())
                .withQuantity(entity.getQuantity())
                .withSubTotal(entity.getSubTotal())
                .withLastChangeTime(entity.getLastChangeTime());
    }
}
