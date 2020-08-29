package io.github.blackfishlabs.forza.ui.addorder.selectcustomer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.domain.pojo.Customer;
import io.github.blackfishlabs.forza.ui.customerlist.CustomerListFragment;
import io.github.blackfishlabs.forza.ui.customerlist.SelectedCustomerEvent;
import io.github.blackfishlabs.forza.ui.orderlist.SelectedOrderEvent;

import static android.text.TextUtils.isEmpty;
import static java.util.Objects.requireNonNull;

public class SelectCustomerStepFragment extends CustomerListFragment implements Step {

    private Customer mSelectedCustomer;

    public static SelectCustomerStepFragment newInstance() {
        return new SelectCustomerStepFragment();
    }

    @Override
    protected int provideContentViewResource() {
        return R.layout.fragment_customer_list;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SelectedOrderEvent event = eventBus().getStickyEvent(SelectedOrderEvent.class);
        if (event != null && event.getOrder() != null) {
            mSelectedCustomer = event.getOrder().getCustomer();
        }
    }

    @Override
    protected void onSelectedCustomerFromList(@NonNull final Customer selectedCustomer) {
        mSelectedCustomer = selectedCustomer;
        eventBus().post(SelectedCustomerEvent.selectCustomer(selectedCustomer));
    }

    @Override
    protected void onRecyclerViewFinishLoading() {
        super.onRecyclerViewFinishLoading();
        if (getView() != null) {
            mSwipeRefreshLayout.setEnabled(false);
            showSelectedCustomer();
        }
    }

    @Override
    public VerificationError verifyStep() {
        if (!isEmpty(mSearchView.getQuery())) {
            mSearchView.setQuery("", false);
            mSearchView.clearFocus();
            mCustomerListAdapter.getFilter().filter("");
        }

        return mSelectedCustomer == null ?
                new VerificationError(getString(R.string.select_customer_step_not_selected_error)) :
                null;
    }

    @Override
    public void onSelected() {
        showSelectedCustomer();
    }

    @Override
    public void onError(@NonNull final VerificationError error) {
        Snackbar.make(requireNonNull(getView()), error.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
    }

    private void showSelectedCustomer() {
        if (mSelectedCustomer != null && mCustomerListAdapter != null) {
            int itemPosition = mCustomerListAdapter.getItemPosition(mSelectedCustomer);
            if (itemPosition != -1) {
                mRecyclerViewCustomers.scrollToPosition(itemPosition);
            }
        }
    }
}
