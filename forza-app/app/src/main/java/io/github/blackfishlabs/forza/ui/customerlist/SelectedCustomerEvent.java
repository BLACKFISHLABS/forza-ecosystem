package io.github.blackfishlabs.forza.ui.customerlist;

import io.github.blackfishlabs.forza.domain.pojo.Customer;

public class SelectedCustomerEvent {

    private final Customer mCustomer;

    private SelectedCustomerEvent(final Customer customer) {
        mCustomer = customer;
    }

    public static SelectedCustomerEvent selectCustomer(final Customer customer) {
        return new SelectedCustomerEvent(customer);
    }

    public Customer getCustomer() {
        return mCustomer;
    }
}
