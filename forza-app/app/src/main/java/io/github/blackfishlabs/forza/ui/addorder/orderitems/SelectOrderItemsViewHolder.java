package io.github.blackfishlabs.forza.ui.addorder.orderitems;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.domain.pojo.OrderItem;
import io.github.blackfishlabs.forza.helper.AndroidUtils;

import static io.github.blackfishlabs.forza.helper.StringUtils.removeAccents;
import static java.util.Objects.requireNonNull;

class SelectOrderItemsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.view_switcher)
    ViewSwitcher viewSwitcher;
    @BindView(R.id.view_switcher_price)
    ViewSwitcher viewSwitcherPrice;
    @BindView(R.id.input_layout_edit_quantity)
    TextInputLayout inputLayoutEditQuantity;
    @BindView(R.id.input_layout_edit_price)
    TextInputLayout inputLayoutEditPrice;
    @BindView(R.id.text_view_quantity)
    TextView textViewQuantity;
    @BindView(R.id.text_view_product_name)
    TextView textViewProductName;
    @BindView(R.id.text_view_product_price)
    TextView textViewProductPrice;
    @BindView(R.id.text_view_item_total)
    TextView textViewItemTotal;
    @BindView(R.id.text_view_product_code)
    TextView textViewProductCode;
    @BindView(R.id.text_view_product_barcode)
    TextView textViewProductBarcode;

    private final SelectOrderItemsCallbacks mItemsCallbacks;

    SelectOrderItemsViewHolder(final View itemView, @Nullable final SelectOrderItemsCallbacks itemsCallbacks) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mItemsCallbacks = itemsCallbacks;
    }

    @OnClick(R.id.text_view_product_price)
    public void onTextViewPriceClicked() {
        viewSwitcherPrice.showNext();
        viewSwitcherPrice.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
                String price = textViewProductPrice.getText().toString();
                String newPrice = removeAccents(price).replaceAll("R\\$", "");
                String replaced = newPrice.replaceAll(",", ".");

                requireNonNull(inputLayoutEditPrice.getEditText()).setText(replaced);
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                AndroidUtils.focusThenShowKeyboard(itemView.getContext(), requireNonNull(inputLayoutEditPrice.getEditText()));
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {
            }
        });
    }

    @OnClick(R.id.text_view_quantity)
    public void onTextViewQuantityClicked() {
        viewSwitcher.showNext();
        viewSwitcher.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
                requireNonNull(inputLayoutEditQuantity.getEditText()).setText(textViewQuantity.getText());
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                AndroidUtils.focusThenShowKeyboard(itemView.getContext(), requireNonNull(inputLayoutEditQuantity.getEditText()));
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {
            }
        });
    }

    @OnEditorAction(R.id.edit_text_edit_price)
    public boolean onEditTextEditPriceAction(KeyEvent key) {
        if (key == null || key.getAction() == KeyEvent.ACTION_UP) {
            final EditText editText = inputLayoutEditPrice.getEditText();
            final EditText editTextQnt = inputLayoutEditQuantity.getEditText();

            AndroidUtils.hideKeyboard(itemView.getContext(), requireNonNull(editText));

            if (!TextUtils.isEmpty(editText.getText())) {
                OrderItem orderItem = (OrderItem) itemView.getTag();

                double price = Double.parseDouble(editText.getText().toString());
                mItemsCallbacks.onChangeOrderItemPriceRequested(orderItem, price, getAdapterPosition());

                float quantity = Float.parseFloat(requireNonNull(editTextQnt).getText().toString());
                mItemsCallbacks.onChangeOrderItemQuantityRequested(orderItem, quantity, getAdapterPosition());
            }

            viewSwitcherPrice.showPrevious();
        }
        return true;
    }

    @OnEditorAction(R.id.edit_text_edit_quantity)
    public boolean onEditTextEditQuantityAction(KeyEvent key) {
        if (key == null || key.getAction() == KeyEvent.ACTION_UP) {
            final EditText editText = inputLayoutEditQuantity.getEditText();
            AndroidUtils.hideKeyboard(itemView.getContext(), requireNonNull(editText));

            if (!TextUtils.isEmpty(editText.getText())) {
                float quantity = Float.parseFloat(editText.getText().toString());
                OrderItem orderItem = (OrderItem) itemView.getTag();
                mItemsCallbacks.onChangeOrderItemQuantityRequested(orderItem, quantity, getAdapterPosition());
            }

            viewSwitcher.showPrevious();
        }
        return true;
    }

    @OnClick(R.id.button_add_item)
    public void onButtonAddItemClicked() {
        OrderItem orderItem = (OrderItem) itemView.getTag();
        mItemsCallbacks.onAddOrderItemRequested(orderItem, getAdapterPosition());
    }

    @OnClick(R.id.button_remove_item)
    public void onButtonRemoveItemClicked() {
        OrderItem orderItem = (OrderItem) itemView.getTag();
        mItemsCallbacks.onRemoveOrderItemRequested(orderItem, getAdapterPosition());
    }

}
