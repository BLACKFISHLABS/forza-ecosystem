package io.github.blackfishlabs.forza.ui.addorder.orderform;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

import io.github.blackfishlabs.forza.domain.pojo.PaymentMethod;
import io.github.blackfishlabs.forza.ui.widget.SingleTextViewAdapter;

class PaymentMethodsAdapter extends SingleTextViewAdapter<PaymentMethod> {

    PaymentMethodsAdapter(@NonNull final Context context, @NonNull final List<PaymentMethod> list) {
        super(context, list);
    }

    @Override
    public long getItemId(final int position) {
        if (position >= 0 && position < getCount()) {
            return getItem(position).getPaymentMethodId();
        } else {
            return -1;
        }
    }

    @Override
    protected String getText(final int position) {
        if (position >= 0 && position < getCount()) {
            return getItem(position).getDescription();
        } else {
            return "";
        }
    }
}
