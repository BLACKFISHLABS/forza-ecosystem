package io.github.blackfishlabs.forza.data.order;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmRepository;
import io.github.blackfishlabs.forza.domain.entity.OrderItemEntity;
import io.github.blackfishlabs.forza.domain.pojo.OrderItem;

public class OrderItemRealmRepository extends RealmRepository<OrderItem, OrderItemEntity> implements OrderItemRepository {

    public OrderItemRealmRepository() {
        super(OrderItemEntity.class, LocalDataInjector.orderItemMapper());
    }
}
