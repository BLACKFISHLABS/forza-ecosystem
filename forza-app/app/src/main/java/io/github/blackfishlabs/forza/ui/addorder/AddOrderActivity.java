package io.github.blackfishlabs.forza.ui.addorder;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.stepstone.stepper.VerificationError;

import org.greenrobot.eventbus.Subscribe;

import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.ui.addorder.orderform.OrderFormStepFragment;
import io.github.blackfishlabs.forza.ui.addorder.orderitems.SelectOrderItemsStepFragment;
import io.github.blackfishlabs.forza.ui.addorder.orderreview.OrderReviewFragment;
import io.github.blackfishlabs.forza.ui.addorder.selectcustomer.SelectCustomerStepFragment;
import io.github.blackfishlabs.forza.ui.base.BaseStepperActivity;
import io.github.blackfishlabs.forza.ui.customerlist.SelectedCustomerEvent;
import io.github.blackfishlabs.forza.ui.orderlist.SelectedOrderEvent;

import static java.util.Objects.requireNonNull;

public class AddOrderActivity extends BaseStepperActivity {

    @Override
    protected int provideContentViewResource() {
        return R.layout.activity_add_order;
    }

    @Override
    protected void onCreate(@Nullable final Bundle inState) {
        super.onCreate(inState);
        setAsInitialFlowActivity();

        SelectedOrderEvent event = eventBus().getStickyEvent(SelectedOrderEvent.class);
        if (event != null && event.getOrder() != null) {
            if (event.isDuplicateOrder()) {
                requireNonNull(getSupportActionBar()).setTitle(R.string.add_order_duplicating_title);
            } else {
                requireNonNull(getSupportActionBar()).setTitle(R.string.add_order_editing_title);
            }
            mStepperLayout.setCurrentStepPosition(mStepperAdapter.getCount() - 2);
        }
    }

    @Override
    protected void provideSteps() {
        mStepperAdapter.addStep(SelectCustomerStepFragment.newInstance(), getString(R.string.add_order_select_customer_step));
        mStepperAdapter.addStep(SelectOrderItemsStepFragment.newInstance(), getString(R.string.add_order_select_items_step));
        mStepperAdapter.addStep(OrderFormStepFragment.newInstance(), getString(R.string.add_order_form_step));
        mStepperAdapter.addStep(OrderReviewFragment.newInstance(), getString(R.string.add_order_review_step));
        mStepperLayout.setOffscreenPageLimit(mStepperAdapter.getCount());
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus().unregister(this);
    }

    @Override
    public void onCompleted(final View completeButton) {
        finish();
    }

    @Override
    public void onError(final VerificationError verificationError) {
    }

    @Subscribe
    public void onSelectedCustomer(SelectedCustomerEvent event) {
        mStepperLayout.proceed();
    }
}
