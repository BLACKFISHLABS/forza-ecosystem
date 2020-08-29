package io.github.blackfishlabs.forza.ui.addorder.orderitems;

import java.util.List;

import io.github.blackfishlabs.forza.domain.pojo.OrderItem;

public class AddedOrderItemsEvent {

    private final List<OrderItem> mOrderItems;

    private AddedOrderItemsEvent(final List<OrderItem> orderItems) {
        mOrderItems = orderItems;
    }

    static AddedOrderItemsEvent newEvent(final List<OrderItem> orderItems) {
        return new AddedOrderItemsEvent(orderItems);
    }

    public List<OrderItem> getOrderItems() {
        return mOrderItems;
    }
}
