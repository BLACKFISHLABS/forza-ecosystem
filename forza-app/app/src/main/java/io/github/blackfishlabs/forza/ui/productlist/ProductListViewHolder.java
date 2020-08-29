package io.github.blackfishlabs.forza.ui.productlist;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.blackfishlabs.forza.R;

class ProductListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view_product_name)
    TextView textViewName;
    @BindView(R.id.text_view_product_price)
    TextView textViewPrice;
    @BindView(R.id.text_view_stock_quantity)
    TextView textViewStockQuantity;

    ProductListViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
