package io.github.blackfishlabs.forza.ui.addorder.orderitems;

import io.github.blackfishlabs.forza.domain.pojo.OrderItem;

interface SelectOrderItemsCallbacks {

    void onAddOrderItemRequested(final OrderItem orderItem, final int position);

    void onRemoveOrderItemRequested(final OrderItem orderItem, final int position);

    void onChangeOrderItemQuantityRequested(final OrderItem orderItem, final float quantity, final int position);

    void onChangeOrderItemPriceRequested(final OrderItem orderItem, final double price, final int position);
}
