package io.github.blackfishlabs.forza.ui.addcustomer;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

import io.github.blackfishlabs.forza.domain.pojo.State;
import io.github.blackfishlabs.forza.ui.widget.SingleTextViewAdapter;

public class StatesAdapter extends SingleTextViewAdapter<State> {

    public StatesAdapter(@NonNull final Context context, @NonNull final List<State> list) {
        super(context, list);
    }

    @Override
    public long getItemId(final int position) {
        if (position >= 0 && position < getCount()) {
            return getItem(position).getStateId();
        } else {
            return -1;
        }
    }

    @Override
    protected String getText(final int position) {
        if (position >= 0 && position < getCount()) {
            return getItem(position).getName();
        } else {
            return "";
        }
    }
}
