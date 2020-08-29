package io.github.blackfishlabs.forza.ui.addcustomer;

import io.github.blackfishlabs.forza.domain.pojo.Customer;

public class SavedCustomerEvent {

    private final Customer mCustomer;

    private SavedCustomerEvent(final Customer customer) {
        mCustomer = customer;
    }

    static SavedCustomerEvent newEvent(final Customer customer) {
        return new SavedCustomerEvent(customer);
    }

    public Customer getCustomer() {
        return mCustomer;
    }
}
