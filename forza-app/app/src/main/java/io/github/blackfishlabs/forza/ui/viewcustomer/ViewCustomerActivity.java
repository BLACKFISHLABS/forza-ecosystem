package io.github.blackfishlabs.forza.ui.viewcustomer;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.domain.enumeration.TypeOfPerson;
import io.github.blackfishlabs.forza.domain.pojo.Customer;
import io.github.blackfishlabs.forza.helper.FormattingUtils;
import io.github.blackfishlabs.forza.helper.Mask;
import io.github.blackfishlabs.forza.ui.base.BaseActivity;
import io.github.blackfishlabs.forza.ui.customerlist.SelectedCustomerEvent;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.requireNonNull;

public class ViewCustomerActivity extends BaseActivity {

    @BindView(R.id.input_layout_person_type)
    TextInputLayout mInputLayoutPersonType;
    @BindView(R.id.input_layout_document)
    TextInputLayout mInputLayoutDocument;
    @BindView(R.id.input_layout_ie)
    TextInputLayout mInputLayoutIe;
    @BindView(R.id.input_layout_customer_name)
    TextInputLayout mInputLayoutSocialName;
    @BindView(R.id.input_layout_fantasy_name)
    TextInputLayout mInputLayoutFantasyName;
    @BindView(R.id.input_layout_buyer)
    TextInputLayout mInputLayoutBuyer;
    @BindView(R.id.input_layout_email)
    TextInputLayout mInputLayoutEmail;
    @BindView(R.id.input_layout_address)
    TextInputLayout mInputLayoutAddress;
    @BindView(R.id.input_layout_address_number)
    TextInputLayout mInputLayoutAddressNumber;
    @BindView(R.id.input_layout_district)
    TextInputLayout mInputLayoutDistrict;
    @BindView(R.id.input_layout_city)
    TextInputLayout mInputLayoutCity;
    @BindView(R.id.input_layout_state)
    TextInputLayout mInputLayoutState;
    @BindView(R.id.input_layout_postal_code)
    TextInputLayout mInputLayoutPostalCode;
    @BindView(R.id.input_layout_address_complement)
    TextInputLayout mInputLayoutAddressComplement;
    @BindView(R.id.input_layout_main_phone)
    TextInputLayout mInputLayoutMainPhone;
    @BindView(R.id.input_layout_other_phone)
    TextInputLayout mInputLayoutOtherPhone;

    @Override
    protected int provideContentViewResource() {
        return R.layout.activity_view_customer;
    }

    @Override
    protected void onCreate(@Nullable final Bundle inState) {
        super.onCreate(inState);
        setAsSubActivity();
        loadCurrentCustomer();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loadCurrentCustomer() {
        SelectedCustomerEvent event = eventBus().getStickyEvent(SelectedCustomerEvent.class);

        if (event != null) {
            Customer mCurrentCustomer = event.getCustomer();

            requireNonNull(getSupportActionBar()).setTitle(R.string.add_customer_view_title);

            TypeOfPerson typeOfPerson = TypeOfPerson.valueOf(mCurrentCustomer.getType());
            requireNonNull(mInputLayoutPersonType.getEditText()).setText(requireNonNull(typeOfPerson).getDescription());
            requireNonNull(mInputLayoutSocialName.getEditText()).setText(mCurrentCustomer.getName());
            requireNonNull(mInputLayoutDocument.getEditText()).setText(
                    FormattingUtils.formatCpforCnpj(Mask.unmask(mCurrentCustomer.getCpfOrCnpj())));

            if (!isNullOrEmpty(mCurrentCustomer.getIe())) {
                requireNonNull(mInputLayoutIe.getEditText()).setText(mCurrentCustomer.getIe());
            }

            if (!isNullOrEmpty(mCurrentCustomer.getBuyer())) {
                requireNonNull(mInputLayoutBuyer.getEditText()).setText(mCurrentCustomer.getBuyer());
            }

            if (!isNullOrEmpty(mCurrentCustomer.getFantasyName())) {
                requireNonNull(mInputLayoutFantasyName.getEditText()).setText(mCurrentCustomer.getFantasyName());
            }

            if (!isNullOrEmpty(mCurrentCustomer.getEmail())) {
                requireNonNull(mInputLayoutEmail.getEditText()).setText(mCurrentCustomer.getEmail());
            }

            if (!isNullOrEmpty(mCurrentCustomer.getAddress())) {
                requireNonNull(mInputLayoutAddress.getEditText()).setText(mCurrentCustomer.getAddress());
            }

            if (!isNullOrEmpty(mCurrentCustomer.getAddressNumber())) {
                requireNonNull(mInputLayoutAddressNumber.getEditText()).setText(mCurrentCustomer.getAddressNumber());
            }

            if (!isNullOrEmpty(mCurrentCustomer.getDistrict())) {
                requireNonNull(mInputLayoutDistrict.getEditText()).setText(mCurrentCustomer.getDistrict());
            }

            requireNonNull(mInputLayoutState.getEditText()).setText(mCurrentCustomer.getCity().getState().getName());
            requireNonNull(mInputLayoutCity.getEditText()).setText(mCurrentCustomer.getCity().getName());

            if (!isNullOrEmpty(mCurrentCustomer.getPostalCode())) {
                requireNonNull(mInputLayoutPostalCode.getEditText()).setText(mCurrentCustomer.getPostalCode());
            }

            if (!isNullOrEmpty(mCurrentCustomer.getAddressComplement())) {
                requireNonNull(mInputLayoutAddressComplement.getEditText()).setText(mCurrentCustomer.getAddressComplement());
            }

            if (!isNullOrEmpty(mCurrentCustomer.getMainPhone())) {
                requireNonNull(mInputLayoutMainPhone.getEditText()).setText(mCurrentCustomer.getMainPhone());
            }

            if (!isNullOrEmpty(mCurrentCustomer.getSecondaryPhone())) {
                requireNonNull(mInputLayoutOtherPhone.getEditText()).setText(mCurrentCustomer.getSecondaryPhone());
            }
        }
    }

}
