package io.github.blackfishlabs.forza.ui.addorder.orderform;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.company.paymentmethod.PaymentMethodsByCompanySpecification;
import io.github.blackfishlabs.forza.data.paymentmethod.PaymentMethodRepository;
import io.github.blackfishlabs.forza.domain.pojo.Customer;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.Order;
import io.github.blackfishlabs.forza.domain.pojo.OrderItem;
import io.github.blackfishlabs.forza.domain.pojo.OrderStatus;
import io.github.blackfishlabs.forza.domain.pojo.OrderType;
import io.github.blackfishlabs.forza.domain.pojo.PaymentMethod;
import io.github.blackfishlabs.forza.domain.pojo.PriceTable;
import io.github.blackfishlabs.forza.helper.CurrencyUtils;
import io.github.blackfishlabs.forza.helper.DateUtils;
import io.github.blackfishlabs.forza.helper.FormattingUtils;
import io.github.blackfishlabs.forza.helper.NumberUtils;
import io.github.blackfishlabs.forza.ui.addorder.orderitems.AddedOrderItemsEvent;
import io.github.blackfishlabs.forza.ui.addorder.orderitems.SelectedPriceTableEvent;
import io.github.blackfishlabs.forza.ui.base.BaseFragment;
import io.github.blackfishlabs.forza.ui.customerlist.SelectedCustomerEvent;
import io.github.blackfishlabs.forza.ui.main.LoggedInUserEvent;
import io.github.blackfishlabs.forza.ui.orderlist.SelectedOrderEvent;

import static android.text.TextUtils.isEmpty;
import static android.widget.AdapterView.INVALID_POSITION;
import static java.util.Objects.requireNonNull;

public class OrderFormStepFragment extends BaseFragment implements Step {

    private PaymentMethodRepository mPaymentMethodRepository;

    private PaymentMethodsAdapter mPaymentMethodsAdapter;

    private LoggedUser mLoggedUser;

    private Order mCurrentOrder;

    @BindView(R.id.input_layout_issue_date)
    TextInputLayout mInputLayoutIssueDate;
    @BindView(R.id.input_layout_customer_name)
    TextInputLayout mInputLayoutCustomerName;
    @BindView(R.id.spinner_payment_method)
    Spinner mSpinnerPaymentMethods;
    @BindView(R.id.input_layout_total_items)
    TextInputLayout mInputLayoutTotalItems;
    @BindView(R.id.input_layout_discount_percentage)
    TextInputLayout mInputLayoutDiscountPercentage;
    @BindView(R.id.input_layout_total_order)
    TextInputLayout mInputLayoutTotalOrder;
    @BindView(R.id.input_layout_observation)
    TextInputLayout mInputLayoutObservation;

    public static OrderFormStepFragment newInstance() {
        return new OrderFormStepFragment();
    }

    @Override
    protected int provideContentViewResource() {
        return R.layout.fragment_order_form;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventBus().register(this);
        loadCurrentOrder();
        loadPaymentMethods();
    }

    @Subscribe(priority = 2)
    public void onSelectedCustomer(SelectedCustomerEvent event) {
        setCustomer(event.getCustomer());
    }

    @Subscribe
    public void onAddedOrderItems(AddedOrderItemsEvent event) {
        setOrderItems(event.getOrderItems());
        calculateDiscount();
    }

    @Subscribe
    public void onSelectedPriceTable(SelectedPriceTableEvent event) {
        setPriceTable(event.getPriceTable());
    }

    @OnTextChanged(R.id.edit_text_discount_percentage)
    void onEditTextDiscountChanged(CharSequence text) {
        setDiscountPercentage(text.toString());
        calculateDiscount();
    }

    @OnItemSelected(R.id.spinner_payment_method)
    void onSpinnerPaymentMethodItemSelected(int position) {
        if (position == INVALID_POSITION) {
            setPaymentMethod(null);
        } else {
            final PaymentMethod paymentMethod = mPaymentMethodsAdapter.getItem(position);
            if (paymentMethod != null) {
                setPaymentMethod(paymentMethod);
                setPaymentMethodFloatingLabelText(paymentMethod.getDiscountPercentage());
            }
        }
    }

    @OnTextChanged(R.id.edit_text_observation)
    void onEditTextObservationChanged(CharSequence text) {
        mCurrentOrder.withObservation(text.toString());
    }

    @Override
    public VerificationError verifyStep() {
        clearInputErrors();

        if (checkEmptyRequiredInput(mSpinnerPaymentMethods)) {
            return new VerificationError(getString(R.string.all_correct_required_fields_error));
        } else if (!checkDiscountValue()) {
            return new VerificationError(getString(R.string.all_correct_fields_error));
        }

        eventBus().postSticky(SelectedOrderEvent.selectOrder(mCurrentOrder));
        return null;
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull final VerificationError error) {
        Snackbar.make(requireNonNull(getView()), error.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
    }

    private void loadCurrentOrder() {
        SelectedOrderEvent event = eventBus().getStickyEvent(SelectedOrderEvent.class);

        // Edit Order
        if (event != null && event.getOrder() != null) {
            if (event.isDuplicateOrder()) {
                mCurrentOrder = event.getOrder().copy();
            } else {
                mCurrentOrder = event.getOrder().withStatus(OrderStatus.STATUS_MODIFIED);
            }

            requireNonNull(mInputLayoutIssueDate.getEditText()).setText(FormattingUtils.formatAsDateTime(mCurrentOrder.getIssueDate()));
            requireNonNull(mInputLayoutCustomerName.getEditText()).setText(mCurrentOrder.getCustomer().getCode().concat("\n").concat(mCurrentOrder.getCustomer().getFantasyName()));
            requireNonNull(mInputLayoutTotalItems.getEditText()).setText(FormattingUtils.formatAsCurrency(mCurrentOrder.getTotalItems()));
            requireNonNull(mInputLayoutDiscountPercentage.getEditText()).setText(String.valueOf(mCurrentOrder.getDiscountPercentage()));
            requireNonNull(mInputLayoutTotalOrder.getEditText()).setText(FormattingUtils.formatAsCurrency(mCurrentOrder.getTotalOrder()));

            if (!isEmpty(mCurrentOrder.getObservation())) {
                requireNonNull(mInputLayoutObservation.getEditText()).setText(mCurrentOrder.getObservation());
            }
        } else { // New Order
            mCurrentOrder = new Order()
                    .withType(OrderType.ORDER_TYPE_NORMAL)
                    .withSalesmanId(getLoggedUser().getSalesman().getSalesmanId())
                    .withCompanyId(getLoggedUser().getDefaultCompany().getCompanyId())
                    .withStatus(OrderStatus.STATUS_CREATED);
            setIssueDate(DateUtils.getCurrentDateTimeInMillis());
            requireNonNull(mInputLayoutDiscountPercentage.getEditText()).setText("0.00");
        }
    }

    private void loadPaymentMethods() {
        if (mPaymentMethodRepository == null) {
            mPaymentMethodRepository = LocalDataInjector.providePaymentMethodRepository();
        }

        List<PaymentMethod> payments = mPaymentMethodRepository.query(new PaymentMethodsByCompanySpecification(getSelectedCompanyId()));
        showPaymentMethods(payments);
    }

    private LoggedUser getLoggedUser() {
        if (mLoggedUser == null) {
            mLoggedUser = eventBus().getStickyEvent(LoggedInUserEvent.class).getUser();
        }
        return mLoggedUser;
    }

    private int getSelectedCompanyId() {
        return getLoggedUser().getDefaultCompany().getCompanyId();
    }

    private void showPaymentMethods(List<PaymentMethod> paymentMethods) {
        mSpinnerPaymentMethods.setAdapter(
                mPaymentMethodsAdapter = new PaymentMethodsAdapter(requireNonNull(getContext()), paymentMethods));

        if (mCurrentOrder != null) {
            int mSelectedPaymentMethod = mPaymentMethodsAdapter.getPosition(mCurrentOrder.getPaymentMethod());
            if (mSelectedPaymentMethod != -1) {
                mSpinnerPaymentMethods.setSelection(mSelectedPaymentMethod + 1, true);
            }
        }
    }

    private void setIssueDate(final long issueDate) {
        mCurrentOrder.withIssueDate(issueDate);
        requireNonNull(mInputLayoutIssueDate.getEditText()).setText(FormattingUtils.formatAsDateTime(issueDate));
    }

    private void setCustomer(final Customer customer) {
        mCurrentOrder.withCustomer(customer);
        requireNonNull(mInputLayoutCustomerName.getEditText()).setText(customer.getCode().concat("\n").concat(customer.getFantasyName()));
    }

    private void setOrderItems(final List<OrderItem> orderItems) {
        mCurrentOrder.withItems(orderItems);
        requireNonNull(mInputLayoutTotalItems.getEditText()).setText(FormattingUtils.formatAsCurrency(mCurrentOrder.getTotalItems()));
    }

    private void setPriceTable(final PriceTable priceTable) {
        mCurrentOrder.withPriceTable(priceTable);
    }

    private void setDiscountPercentage(final String discountStr) {
        mCurrentOrder.withDiscountPercentage(NumberUtils.toFloat(discountStr));
    }

    private void calculateDiscount() {
        final float discountPercentage = mCurrentOrder.getDiscountPercentage();
        final double discount = CurrencyUtils.round(mCurrentOrder.getTotalItems() * discountPercentage / 100);
        mCurrentOrder.withDiscount(discount);

        requireNonNull(mInputLayoutTotalOrder.getEditText()).setText(
                FormattingUtils.formatAsCurrency(mCurrentOrder.getTotalOrder()));
    }

    private void setPaymentMethod(final PaymentMethod paymentMethod) {
        mCurrentOrder.withPaymentMethod(paymentMethod);
    }

    private void setPaymentMethodFloatingLabelText(final Float discountPercentage) {
        // mSpinnerPaymentMethods.setFloatingLabelText(getString(R.string.order_form_payment_method_label, getString(R.string.order_form_no_discount)));
        // mSpinnerPaymentMethods.setFloatingLabelText(getString(R.string.order_form_payment_method_label, FormattingUtils.formatAsPercent(discountPercentage)));
    }

    private void clearInputErrors() {
        mInputLayoutDiscountPercentage.setError(null);
        // mSpinnerPaymentMethods.setError(null);
    }

    private boolean checkEmptyRequiredInput(Spinner spinner) {
        // spinner.setError(getString(R.string.all_required_field_error));
        return spinner.getSelectedItem() == null;
    }

    private boolean checkDiscountValue() {
        final String discountPercentageStr = requireNonNull(mInputLayoutDiscountPercentage.getEditText()).getText().toString();

        if (isEmpty(discountPercentageStr)) {
            return true;
        }

        final double discountPercentage = NumberUtils.toDouble(discountPercentageStr);

        if (discountPercentage == 0) {
            return true;
        }

        if (!getLoggedUser().getSalesman().getCanApplyDiscount()) {
            mInputLayoutDiscountPercentage.setError(getString(R.string.order_form_salesman_cant_apply_discount));
            return false;
        }

        final int selectedPaymentMethodPosition = mSpinnerPaymentMethods.getSelectedItemPosition();

        if (selectedPaymentMethodPosition == INVALID_POSITION) {
            return true;
        }

        PaymentMethod paymentMethod = mPaymentMethodsAdapter.getItem(selectedPaymentMethodPosition);

        //noinspection ConstantConditions
        if (paymentMethod.getDiscountPercentage() == 0) {
            mInputLayoutDiscountPercentage.setError(getString(R.string.order_form_payment_method_with_no_discount));
            return false;
        }

        if (discountPercentage > paymentMethod.getDiscountPercentage()) {
            mInputLayoutDiscountPercentage.setError(getString(R.string.order_form_discount_value_not_allowed));
            return false;
        }

        return true;
    }

}
