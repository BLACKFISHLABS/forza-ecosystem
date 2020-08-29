package io.github.blackfishlabs.forza.ui.vieworder;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import butterknife.BindView;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.domain.pojo.Order;
import io.github.blackfishlabs.forza.domain.pojo.OrderItem;
import io.github.blackfishlabs.forza.domain.pojo.OrderStatus;
import io.github.blackfishlabs.forza.helper.FormattingUtils;
import io.github.blackfishlabs.forza.helper.NumberUtils;
import io.github.blackfishlabs.forza.ui.base.BaseActivity;
import io.github.blackfishlabs.forza.ui.orderlist.SelectedOrderEvent;

import static android.text.TextUtils.isEmpty;
import static java.util.Objects.requireNonNull;

public class ViewOrderActivity extends BaseActivity {

    @BindView(R.id.edit_text_issue_date)
    EditText editTextIssueDate;
    @BindView(R.id.edit_text_customer_name)
    EditText editTextCustomerName;
    @BindView(R.id.edit_text_total_items)
    EditText editTextTotalItems;
    @BindView(R.id.input_layout_payment_method)
    TextInputLayout inputLayoutPaymentMethod;
    @BindView(R.id.edit_text_discount_percentage)
    EditText editTextDiscountPercentage;
    @BindView(R.id.edit_text_total_order)
    EditText editTextTotalOrder;
    @BindView(R.id.edit_text_observation)
    EditText editTextObservation;
    @BindView(R.id.orderSummaryListView)
    ListView orderSummary;

    @Override
    protected int provideContentViewResource() {
        return R.layout.activity_view_order;
    }

    @Override
    protected void onCreate(@Nullable final Bundle inState) {
        super.onCreate(inState);
        setAsSubActivity();
        loadCurrentOrder();
    }

    private void loadCurrentOrder() {
        SelectedOrderEvent event = eventBus().getStickyEvent(SelectedOrderEvent.class);
        if (event != null && event.getOrder() != null) {
            Order order = event.getOrder();
            Integer idOrder = order.getOrderId() != null ? order.getOrderId() : order.getId();

            requireNonNull(getSupportActionBar())
                    .setTitle(getString(R.string.title_activity_view_order, idOrder));

            switch (order.getStatus()) {
                case OrderStatus.STATUS_CREATED: {
                    getSupportActionBar().setSubtitle(R.string.order_status_created);
                    break;
                }
                case OrderStatus.STATUS_MODIFIED: {
                    getSupportActionBar().setSubtitle(R.string.order_status_updated);
                    break;
                }
                case OrderStatus.STATUS_SYNCED: {
                    getSupportActionBar().setSubtitle(R.string.order_status_synced);
                    break;
                }
                case OrderStatus.STATUS_CANCELLED: {
                    getSupportActionBar().setSubtitle(R.string.order_status_cancelled);
                    break;
                }
                case OrderStatus.STATUS_INVOICED: {
                    getSupportActionBar().setSubtitle(R.string.order_status_invoiced);
                    break;
                }
            }

            editTextIssueDate.setText(FormattingUtils.formatAsDateTime(order.getIssueDate()));
            editTextCustomerName.setText(order.getCustomer().getCode().concat("\n").concat(order.getCustomer().getFantasyName()));
            editTextTotalItems.setText(FormattingUtils.formatAsCurrency(order.getTotalItems()));
            requireNonNull(inputLayoutPaymentMethod.getEditText())
                    .setText(order.getPaymentMethod().getDescription());

            if (order.getPaymentMethod().getDiscountPercentage() == 0) {
                inputLayoutPaymentMethod.setHint(
                        getString(R.string.order_form_payment_method_label,
                                getString(R.string.order_form_no_discount)));
            } else {
                inputLayoutPaymentMethod.setHint(
                        getString(R.string.order_form_payment_method_label,
                                FormattingUtils.formatAsPercent(order.getPaymentMethod().getDiscountPercentage())));
            }

            editTextDiscountPercentage.setText(FormattingUtils.formatAsPercent(order.getDiscountPercentage()));
            editTextTotalOrder.setText(FormattingUtils.formatAsCurrency(order.getTotalOrder()));

            if (!isEmpty(order.getObservation())) {
                editTextObservation.setText(order.getObservation());
            }

            ArrayList<String> dishesDisplay = new ArrayList<>();
            for (OrderItem item : order.getItems()) {
                String dishCode = item.getItem().getProduct().getCode();
                String dishDescription = item.getItem().getProduct().getDescription();
                String dishQuantity = FormattingUtils.formatAsNumber(item.getQuantity());
                String dishSalesPrice = FormattingUtils.formatAsCurrency(NumberUtils.withDefaultValue(item.getSalesPrice(), 0));
                String dishSubTotal = FormattingUtils.formatAsCurrency(NumberUtils.withDefaultValue(item.getSubTotal(), 0));

                String format = "%s ............ %s%n";
                String amountFormat = String.format(format, "quantidade ", dishQuantity);
                String valueFormat = String.format(format, "valor unit.   ", dishSalesPrice);
                String totalFormat = String.format(format, "subtotal      ", dishSubTotal);

                String dishInfoString =
                        dishCode.concat(" - ").concat(dishDescription)
                                .concat("\n")
                                .concat(amountFormat)
                                .concat(valueFormat)
                                .concat(totalFormat);
                dishesDisplay.add(dishInfoString);
            }

            String[] items = dishesDisplay.toArray(new String[0]);

            TextView textView = new TextView(this);
            String title = "PRODUTOS SELECIONADOS";
            textView.setText(title.toUpperCase());
            orderSummary.addHeaderView(textView);

            orderSummary.setAdapter(new ArrayAdapter<String>(requireNonNull(this),
                    android.R.layout.simple_list_item_1, items) {
                @NonNull
                @Override
                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                    return super.getView(position, convertView, parent);
                }
            });

            eventBus().removeStickyEvent(SelectedOrderEvent.class);
        }
    }
}
