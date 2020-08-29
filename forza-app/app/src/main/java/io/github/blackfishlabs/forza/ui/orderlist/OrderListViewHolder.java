package io.github.blackfishlabs.forza.ui.orderlist;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.blackfishlabs.forza.R;

class OrderListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view_order_number)
    TextView textViewOrderNumber;
    @BindView(R.id.text_view_customer_name)
    TextView textViewCustomerName;
    @BindView(R.id.text_view_total_order)
    TextView textViewTotalOrder;
    @BindView(R.id.text_view_order_date)
    TextView textViewOrderDate;
    @BindView(R.id.view_order_status)
    View viewOrderStatus;

    OrderListViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
