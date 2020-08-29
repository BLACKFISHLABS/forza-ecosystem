package io.github.blackfishlabs.forza.data.sync;

public class CustomersSyncedEvent {

    private CustomersSyncedEvent() {
    }

    static CustomersSyncedEvent customersSynced() {
        return new CustomersSyncedEvent();
    }
}
