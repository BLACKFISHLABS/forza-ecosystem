package io.github.blackfishlabs.forza.ui.orderlist;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.util.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.domain.pojo.Order;
import io.github.blackfishlabs.forza.helper.DrawableUtils;
import io.github.blackfishlabs.forza.helper.FormattingUtils;
import io.github.blackfishlabs.forza.helper.OrderUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

class OrderAdapterItem extends AbstractItem<OrderAdapterItem, OrderAdapterItem.ViewHolder> {

    private final boolean showStatusIndicator;
    private Order order;

    private OrderAdapterItem(final boolean showStatusIndicator) {
        this.showStatusIndicator = showStatusIndicator;
    }

    static OrderAdapterItem create(final boolean showStatusIndicator) {
        return new OrderAdapterItem(showStatusIndicator);
    }

    public OrderAdapterItem withOrder(final Order order) {
        this.order = order;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public ViewHolder getViewHolder(final View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.order_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.list_item_order;
    }

    @Override
    public void bindView(final ViewHolder holder, final List<Object> payloads) {
        super.bindView(holder, payloads);

        final Context context = holder.itemView.getContext();

        if (order.getOrderId() != null && order.getOrderId() != 0) {
            holder.textViewOrderNumber.setText(context.getString(R.string.order_list_template_text_order_number,
                    order.getOrderId()));
        } else {
            holder.textViewOrderNumber.setText(context.getString(R.string.orders_report_text_no_order_number));
        }

        holder.textViewCustomerName.setText(context.getString(R.string.order_list_template_text_customer_name,
                order.getCustomer().getFantasyName()));

        final double totalItems = order.getTotalOrder();
        holder.textViewTotalOrder.setText(context.getString(R.string.order_list_template_text_order_total,
                FormattingUtils.formatAsCurrency(totalItems)));

        holder.textViewOrderDate.setText(context.getString(R.string.order_list_template_text_order_date,
                FormattingUtils.formatAsDateTime(order.getIssueDate())));

        holder.viewOrderStatus.setVisibility(showStatusIndicator ? VISIBLE : GONE);
        if (showStatusIndicator) {
            DrawableUtils.changeDrawableBackground(context, holder.viewOrderStatus.getBackground(),
                    OrderUtils.getStatusColor(order.getStatus()));
        }

        UIUtils.setBackground(holder.itemView, FastAdapterUIUtils.getSelectableBackground(context,
                ContextCompat.getColor(context, R.color.color_accent), true));
    }

    @Override
    public void unbindView(final ViewHolder holder) {
        super.unbindView(holder);
        holder.textViewOrderNumber.setText(null);
        holder.textViewCustomerName.setText(null);
        holder.textViewTotalOrder.setText(null);
        holder.textViewOrderDate.setText(null);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

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

        ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
