package io.github.blackfishlabs.forza.data.sync;

public class ProductsUpdatedEvent {

    private ProductsUpdatedEvent() {
    }

    static ProductsUpdatedEvent productsUpdated() {
        return new ProductsUpdatedEvent();
    }
}
