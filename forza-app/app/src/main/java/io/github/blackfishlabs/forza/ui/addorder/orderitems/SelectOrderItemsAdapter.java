package io.github.blackfishlabs.forza.ui.addorder.orderitems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.domain.pojo.OrderItem;
import io.github.blackfishlabs.forza.domain.pojo.PriceTableItem;
import io.github.blackfishlabs.forza.domain.pojo.Product;
import io.github.blackfishlabs.forza.helper.FormattingUtils;
import io.github.blackfishlabs.forza.helper.NumberUtils;
import io.github.blackfishlabs.forza.ui.base.BaseFilter;

import static java.util.Objects.requireNonNull;

class SelectOrderItemsAdapter extends RecyclerView.Adapter<SelectOrderItemsViewHolder> implements Filterable {

    private final List<OrderItem> orderItems;

    private final SelectOrderItemsCallbacks itemsCallbacks;

    private final Context context;

    private List<OrderItem> originalOrderItems;

    private OrderItemListFilter filter;

    SelectOrderItemsAdapter(final List<OrderItem> orderItems,
                            final SelectOrderItemsCallbacks itemsCallbacks, final Context context) {
        this.orderItems = orderItems;
        this.itemsCallbacks = itemsCallbacks;
        this.context = context;
    }

    @Override
    public SelectOrderItemsViewHolder onCreateViewHolder(
            final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(context)
                .inflate(R.layout.list_item_order_item, parent, false);
        return new SelectOrderItemsViewHolder(itemView, itemsCallbacks);
    }

    @Override
    public void onBindViewHolder(
            final SelectOrderItemsViewHolder holder, final int position) {
        final OrderItem orderItem = orderItems.get(position);
        final PriceTableItem item = orderItem.getItem();
        final Product product = item.getProduct();

        holder.textViewProductName.setText(product.getDescription());
        holder.textViewProductPrice.setText(
                context.getString(R.string.select_order_items_template_product_price,
                        FormattingUtils.formatAsCurrency(NumberUtils.withDefaultValue(orderItem.getSalesPrice(), 0))));
        holder.textViewItemTotal.setText(
                context.getString(R.string.select_order_items_template_item_total,
                        FormattingUtils.formatAsCurrency(NumberUtils.withDefaultValue(orderItem.getSubTotal(), 0))));
        holder.textViewQuantity.setText(
                FormattingUtils.formatAsNumber(NumberUtils.withDefaultValue(orderItem.getQuantity(), 0)));
        requireNonNull(holder.inputLayoutEditQuantity.getEditText()).setText(holder.textViewQuantity.getText());
        holder.textViewProductCode.setText(
                context.getString(R.string.select_order_items_template_product_code,
                        product.getCode()));
        holder.textViewProductBarcode.setText(
                context.getString(R.string.select_order_items_template_product_barcode,
                        product.getBarCode()));

        holder.itemView.setTag(orderItem);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new OrderItemListFilter();
        }
        return filter;
    }

    public boolean isEmptyList() {
        return getItemCount() == 0;
    }

    private class OrderItemListFilter extends BaseFilter<OrderItem> {

        OrderItemListFilter() {
            super(SelectOrderItemsAdapter.this, orderItems, originalOrderItems);
        }

        @Override
        protected String[] filterValues(final OrderItem orderItem) {
            final Product product = orderItem.getItem().getProduct();
            return new String[]{product.getDescription(), product.getBarCode(),
                    product.getCode(), product.getBarCode()};
        }
    }
}
