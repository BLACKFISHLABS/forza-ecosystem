package io.github.blackfishlabs.forza.ui.citylist;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.blackfishlabs.forza.R;

class CityListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view_city_name)
    TextView textViewName;

    CityListViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
