package io.github.blackfishlabs.forza.ui.addorder.orderitems;

import io.github.blackfishlabs.forza.domain.pojo.PriceTable;

public class SelectedPriceTableEvent {

    private final PriceTable mPriceTable;

    private SelectedPriceTableEvent(final PriceTable priceTable) {
        mPriceTable = priceTable;
    }

    static SelectedPriceTableEvent newEvent(final PriceTable priceTable) {
        return new SelectedPriceTableEvent(priceTable);
    }

    public PriceTable getPriceTable() {
        return mPriceTable;
    }
}
