package io.github.blackfishlabs.forza.ui.addorder.orderreview;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.order.OrderRepository;
import io.github.blackfishlabs.forza.domain.pojo.Order;
import io.github.blackfishlabs.forza.domain.pojo.OrderItem;
import io.github.blackfishlabs.forza.helper.FormattingUtils;
import io.github.blackfishlabs.forza.helper.NumberUtils;
import io.github.blackfishlabs.forza.ui.base.BaseFragment;
import io.github.blackfishlabs.forza.ui.orderlist.SelectedOrderEvent;

import static android.text.TextUtils.isEmpty;
import static java.util.Objects.requireNonNull;

public class OrderReviewFragment extends BaseFragment implements BlockingStep {

    private OrderRepository mOrderRepository;
    private MaterialDialog mProgressDialog;
    private Order mCurrentOrder;

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

    public static OrderReviewFragment newInstance() {
        return new OrderReviewFragment();
    }

    @Override
    protected int provideContentViewResource() {
        return R.layout.fragment_order_review;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventBus().register(this);

        TextView textView = new TextView(getContext());
        String title = "PRODUTOS SELECIONADOS";
        textView.setText(title.toUpperCase());
        orderSummary.addHeaderView(textView);
    }

    private void loadCurrentOrder() {
        editTextIssueDate.setText(FormattingUtils.formatAsDateTime(mCurrentOrder.getIssueDate()));
        editTextCustomerName.setText(mCurrentOrder.getCustomer().getCode().concat("\n").concat(mCurrentOrder.getCustomer().getFantasyName()));
        editTextTotalItems.setText(FormattingUtils.formatAsCurrency(mCurrentOrder.getTotalItems()));
        requireNonNull(inputLayoutPaymentMethod.getEditText())
                .setText(mCurrentOrder.getPaymentMethod().getDescription());

        if (mCurrentOrder.getPaymentMethod().getDiscountPercentage() == 0) {
            inputLayoutPaymentMethod.setHint(
                    getString(R.string.order_form_payment_method_label,
                            getString(R.string.order_form_no_discount)));
        } else {
            inputLayoutPaymentMethod.setHint(
                    getString(R.string.order_form_payment_method_label,
                            FormattingUtils.formatAsPercent(mCurrentOrder.getPaymentMethod().getDiscountPercentage())));
        }

        editTextDiscountPercentage.setText(FormattingUtils.formatAsPercent(mCurrentOrder.getDiscountPercentage()));
        editTextTotalOrder.setText(FormattingUtils.formatAsCurrency(mCurrentOrder.getTotalOrder()));

        if (!isEmpty(mCurrentOrder.getObservation())) {
            editTextObservation.setText(mCurrentOrder.getObservation());
        }

        ArrayList<String> dishesDisplay = new ArrayList<>();
        for (OrderItem item : mCurrentOrder.getItems()) {
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
        orderSummary.setAdapter(new ArrayAdapter<String>(requireNonNull(getContext()),
                android.R.layout.simple_list_item_1, items) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        });
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @UiThread
    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle("Antes de continuar!");
        alert.setMessage("Confira a venda e suas informações");
        alert.setCancelable(false);
        alert
                .setNegativeButton("voltar", (dialogInterface, i) -> {
                })
                .setPositiveButton("conferida, pode gravar",
                        (dialogInterface, i) -> subscribeToSaveOrder(callback));

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void subscribeToSaveOrder(final StepperLayout.OnCompleteClickedCallback callback) {
        if (mOrderRepository == null) {
            mOrderRepository = LocalDataInjector.providerOrderRepository();
        }

        showProgressDialog();
        saveOrderInDataBase();
        showSuccessSavingOrder(callback);
    }

    private void saveOrderInDataBase() {
        mOrderRepository.save(mCurrentOrder);
    }

    private void showSuccessSavingOrder(final StepperLayout.OnCompleteClickedCallback callback) {
        hideProgressDialog();
        Snackbar.make(requireNonNull(getView()), R.string.order_form_saved_successfully, Snackbar.LENGTH_SHORT)
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(final Snackbar snackbar, final int event) {
                        eventBus().postSticky(SavedOrderEvent.newEvent(mCurrentOrder));
                        callback.complete();
                    }
                })
                .show();
    }

    private void showProgressDialog() {
        mProgressDialog = new MaterialDialog.Builder(requireNonNull(getContext()))
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .title(R.string.all_saving_message)
                .content(R.string.all_please_wait)
                .cancelable(true)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Snackbar.make(requireNonNull(getView()), error.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        eventBus().removeStickyEvent(SelectedOrderEvent.class);
        eventBus().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void onSelectedOrder(SelectedOrderEvent event) {
        if (event != null) {
            mCurrentOrder = event.getOrder();
            loadCurrentOrder();
        }
    }
}
