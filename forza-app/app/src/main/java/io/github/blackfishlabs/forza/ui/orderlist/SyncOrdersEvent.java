package io.github.blackfishlabs.forza.ui.orderlist;

import java.util.List;

import io.github.blackfishlabs.forza.domain.pojo.Order;

public class SyncOrdersEvent {

    private final List<Order> orders;

    private SyncOrdersEvent(final List<Order> orders) {
        this.orders = orders;
    }

    static SyncOrdersEvent just(final List<Order> orders) {
        return new SyncOrdersEvent(orders);
    }

    public List<Order> getOrders() {
        return orders;
    }
}
