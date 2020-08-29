package io.github.blackfishlabs.forza.ui.addcustomer;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

import io.github.blackfishlabs.forza.domain.enumeration.TypeOfPerson;
import io.github.blackfishlabs.forza.ui.widget.SingleTextViewAdapter;

class TypeOfPersonAdapter extends SingleTextViewAdapter<TypeOfPerson> {

    TypeOfPersonAdapter(@NonNull final Context context, @NonNull final List<TypeOfPerson> list) {
        super(context, list);
    }

    @Override
    public long getItemId(int position) {
        if (position >= 0 && position < getCount()) {
            return getItem(position).getOrdinalType();
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
