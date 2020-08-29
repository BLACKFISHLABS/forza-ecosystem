package io.github.blackfishlabs.forza.ui.addorder.orderreview;

import io.github.blackfishlabs.forza.domain.pojo.Order;

public class SavedOrderEvent {

    private final Order mOrder;

    private SavedOrderEvent(final Order order) {
        mOrder = order;
    }

    static SavedOrderEvent newEvent(final Order order) {
        return new SavedOrderEvent(order);
    }

    public Order getOrder() {
        return mOrder;
    }
}
