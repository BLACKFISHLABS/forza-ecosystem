package io.github.blackfishlabs.forza.data.order;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmRepository;
import io.github.blackfishlabs.forza.domain.entity.OrderEntity;
import io.github.blackfishlabs.forza.domain.pojo.Order;

public class OrderRealmRepository extends RealmRepository<Order, OrderEntity> implements OrderRepository {

    public OrderRealmRepository() {
        super(OrderEntity.class, LocalDataInjector.orderMapper());
    }
}
