package io.github.blackfishlabs.forza.data.sync;

public class OrdersSyncedEvent {

    private final boolean instantly;

    private OrdersSyncedEvent(final boolean instantly) {
        this.instantly = instantly;
    }

    static OrdersSyncedEvent ordersSyncedBySchedule() {
        return new OrdersSyncedEvent(false);
    }

    static OrdersSyncedEvent ordersInstantlySynced() {
        return new OrdersSyncedEvent(true);
    }

    public boolean isInstantly() {
        return instantly;
    }
}
