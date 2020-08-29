package io.github.blackfishlabs.forza.ui.addcustomer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Spinner;
import android.widget.TableRow;

import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.RemoteDataInjector;
import io.github.blackfishlabs.forza.data.city.CityByNameSpecification;
import io.github.blackfishlabs.forza.data.city.CityRepository;
import io.github.blackfishlabs.forza.data.city.StateRepository;
import io.github.blackfishlabs.forza.data.company.customer.CompanyCustomerRepository;
import io.github.blackfishlabs.forza.data.customer.CustomerRepository;
import io.github.blackfishlabs.forza.domain.entity.StateEntity;
import io.github.blackfishlabs.forza.domain.enumeration.TypeOfPerson;
import io.github.blackfishlabs.forza.domain.pojo.City;
import io.github.blackfishlabs.forza.domain.pojo.Company;
import io.github.blackfishlabs.forza.domain.pojo.CompanyCustomer;
import io.github.blackfishlabs.forza.domain.pojo.Customer;
import io.github.blackfishlabs.forza.domain.pojo.CustomerStatus;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.PostalCode;
import io.github.blackfishlabs.forza.domain.pojo.State;
import io.github.blackfishlabs.forza.helper.Mask;
import io.github.blackfishlabs.forza.helper.ValidationUtils;
import io.github.blackfishlabs.forza.ui.base.BaseActivity;
import io.github.blackfishlabs.forza.ui.citylist.SelectedCityEvent;
import io.github.blackfishlabs.forza.ui.customerlist.SelectedCustomerEvent;
import io.github.blackfishlabs.forza.ui.main.LoggedInUserEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.text.TextUtils.isEmpty;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.requireNonNull;

public class AddCustomerActivity extends BaseActivity {

    private TypeOfPersonAdapter mTypeOfPersonAdapter;
    private StatesAdapter mStatesAdapter;
    private StateRepository mStateRepository;
    private CityRepository mCityRepository;
    private Customer mCurrentCustomer;
    private CustomerRepository mCustomerRepository;
    private CompanyCustomerRepository mCompanyCustomerRepository;

    private MaterialDialog mProgressDialog;

    private boolean mIsEdition;

    private LoggedUser mLoggedUser;

    @BindView(R.id.spinner_type_of_person)
    Spinner mSpinnerTypeOfPerson;
    @BindView(R.id.input_layout_cnpj)
    TextInputLayout mInputLayoutCnpj;
    @BindView(R.id.input_layout_cpf)
    TextInputLayout mInputLayoutCpf;
    @BindView(R.id.input_layout_customer_name)
    TextInputLayout mInputLayoutSocialName;
    @BindView(R.id.input_layout_fantasy_name)
    TextInputLayout mInputLayoutFantasyName;
    @BindView(R.id.input_layout_email)
    TextInputLayout mInputLayoutEmail;
    @BindView(R.id.input_layout_address)
    TextInputLayout mInputLayoutAddress;
    @BindView(R.id.input_layout_address_number)
    TextInputLayout mInputLayoutAddressNumber;
    @BindView(R.id.input_layout_district)
    TextInputLayout mInputLayoutDistrict;
    @BindView(R.id.spinner_state)
    Spinner mSpinnerState;
    @BindView(R.id.input_layout_city)
    TextInputLayout mInputLayoutCity;
    @BindView(R.id.input_layout_postal_code)
    TextInputLayout mInputLayoutPostalCode;
    @BindView(R.id.input_layout_address_complement)
    TextInputLayout mInputLayoutAddressComplement;
    @BindView(R.id.input_layout_main_phone)
    TextInputLayout mInputLayoutMainPhone;
    @BindView(R.id.input_layout_other_phone)
    TextInputLayout mInputLayoutOtherPhone;

    @BindView(R.id.edit_text_cnpj)
    TextInputEditText mInputEditTextCnpj;
    @BindView(R.id.edit_text_cpf)
    TextInputEditText mInputEditTextCpf;
    @BindView(R.id.edit_text_postal_code)
    TextInputEditText mInputEditTextPostalCode;
    @BindView(R.id.edit_text_main_phone)
    TextInputEditText mInputEditTextPhone;
    @BindView(R.id.edit_text_other_phone)
    TextInputEditText mInputEditTextOtherPhone;

    @BindView(R.id.cnpj)
    TableRow mTableRowCNPJ;
    @BindView(R.id.cpf)
    TableRow mTableRowCPF;

    @Override
    protected int provideContentViewResource() {
        return R.layout.activity_add_customer;
    }

    @Override
    protected void onCreate(@Nullable final Bundle inState) {
        super.onCreate(inState);
        setAsInitialFlowActivity();
        setUpViews();
        loadCurrentCustomer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus().register(this);
        loadStates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_customer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.action_all_done) {
            saveCustomer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnItemSelected(R.id.spinner_type_of_person)
    void onSpinnerTypeOfPersonItemSelected(int position) {
        if (position == Spinner.INVALID_POSITION) {
            mCurrentCustomer.withType(null);
        } else {
            final TypeOfPerson typeOfPerson = mTypeOfPersonAdapter.getItem(position);
            if (typeOfPerson != null) {
                mCurrentCustomer.withType(typeOfPerson.getOrdinalType());

                if (typeOfPerson.equals(TypeOfPerson.PRIVATE_INDIVIDUAL)) {
                    mTableRowCNPJ.setVisibility(View.GONE);
                    mTableRowCPF.setVisibility(View.VISIBLE);

                    mInputEditTextCpf.requestFocus();
                } else if (typeOfPerson.equals(TypeOfPerson.LEGAL_ENTITY)) {
                    mTableRowCNPJ.setVisibility(View.VISIBLE);
                    mTableRowCPF.setVisibility(View.GONE);

                    mInputEditTextCnpj.requestFocus();
                }
            }
        }
    }

    @OnTextChanged(R.id.edit_text_cnpj)
    void onEditTextCnpjChanged(CharSequence text) {
        mCurrentCustomer.withCpfOrCnpj(text.toString());
    }

    @OnTextChanged(R.id.edit_text_cpf)
    void onEditTextCpfChanged(CharSequence text) {
        mCurrentCustomer.withCpfOrCnpj(text.toString());
    }

    @OnTextChanged(R.id.edit_text_customer_name)
    void onEditTextCustomerNameChanged(CharSequence text) {
        mCurrentCustomer.withName(text.toString());
    }

    @OnTextChanged(R.id.edit_text_fantasy_name)
    void onEditTextFantasyNameChanged(CharSequence text) {
        mCurrentCustomer.withFantasyName(text.toString());
    }

    @OnTextChanged(R.id.edit_text_email)
    void onEditTextEmailChanged(CharSequence text) {
        mCurrentCustomer.withEmail(text.toString());
    }

    @OnTextChanged(R.id.edit_text_address)
    void onEditTextAddressChanged(CharSequence text) {
        mCurrentCustomer.withAddress(text.toString());
    }

    @OnTextChanged(R.id.edit_text_address_number)
    void onEditTextAddressNumberChanged(CharSequence text) {
        mCurrentCustomer.withAddressNumber(text.toString());
    }

    @OnTextChanged(R.id.edit_text_district)
    void onEditTextDistrictChanged(CharSequence text) {
        mCurrentCustomer.withDistrict(text.toString());
    }

    @OnItemSelected(R.id.spinner_state)
    void onSpinnerStateItemSelected(int position) {
        if (position == Spinner.INVALID_POSITION) {
            mCurrentCustomer.withCity(null);
            requireNonNull(mInputLayoutCity.getEditText()).setText("");
            return;
        }

        if (mCurrentCustomer.getCity() != null) {
            final State stateSelected = mStatesAdapter.getItem(position);
            if (stateSelected != null) {
                if (!stateSelected.equals(mCurrentCustomer.getCity().getState())) {
                    mCurrentCustomer.withCity(null);
                    requireNonNull(mInputLayoutCity.getEditText()).setText("");
                }
            }
        }
    }

    @OnTouch(R.id.edit_text_city)
    boolean onEditTextCityTouched(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mSpinnerState.getSelectedItem() == null) {
                // mSpinnerState.setError(R.string.add_customer_select_state_error);
            } else {
                selectState(mSpinnerState.getSelectedItemPosition());
            }
            return true;
        }
        return false;
    }

    @Subscribe(sticky = true)
    public void onSelectedCity(SelectedCityEvent event) {
        mCurrentCustomer.withCity(event.getCity());
        mInputLayoutCity.getEditText().setText(event.getCity().getName());
        eventBus().removeStickyEvent(SelectedCityEvent.class);
    }

    @OnTextChanged(R.id.edit_text_city)
    void onEditTextCityChanged(CharSequence text) {
        mInputLayoutCity.setHint(isEmpty(text) ?
                getString(R.string.add_customer_select_city_hint) :
                getString(R.string.add_customer_city_selected_hint));
    }

    @OnTextChanged(R.id.edit_text_postal_code)
    void onEditTextPostalCodeChanged(CharSequence text) {
        mCurrentCustomer.withPostalCode(text.toString());
    }

    @OnTextChanged(R.id.edit_text_address_complement)
    void onEditTextAddressComplementChanged(CharSequence text) {
        mCurrentCustomer.withAddressComplement(text.toString());
    }

    @OnTextChanged(R.id.edit_text_main_phone)
    void onEditTextMainPhoneChanged(CharSequence text) {
        mCurrentCustomer.withMainPhone(text.toString());
    }

    @OnTextChanged(R.id.edit_text_other_phone)
    void onEditTextOtherPhoneChanged(CharSequence text) {
        mCurrentCustomer.withSecondaryPhone(text.toString());
    }

    private void setUpViews() {
        mSpinnerTypeOfPerson.setAdapter(mTypeOfPersonAdapter = new TypeOfPersonAdapter(this, Arrays.asList(TypeOfPerson.values())));
        mSpinnerTypeOfPerson.setSelection(TypeOfPerson.LEGAL_ENTITY.getOrdinalType() + 1, true);

        mInputEditTextCpf.addTextChangedListener(Mask.insert("###.###.###-##", mInputEditTextCpf));
        mInputEditTextCnpj.addTextChangedListener(Mask.insert("##.###.###/####-##", mInputEditTextCnpj));

        mInputEditTextPostalCode.addTextChangedListener(Mask.insert("##.###-###", mInputEditTextPostalCode));

        mInputEditTextPhone.addTextChangedListener(Mask.insert("(##)#.####-####", mInputEditTextPhone));
        mInputEditTextOtherPhone.addTextChangedListener(Mask.insert("(##)#.####-####", mInputEditTextOtherPhone));

        mInputEditTextPostalCode.setOnFocusChangeListener((view1, b) -> {
            if (!b) {
                executeSearchPostalCode(Mask.unmask(mInputEditTextPostalCode.getText().toString()));
            }
        });
    }

    private void loadCurrentCustomer() {
        SelectedCustomerEvent event = eventBus().getStickyEvent(SelectedCustomerEvent.class);

        if (event != null) {
            mCurrentCustomer = event.getCustomer();
            mIsEdition = true;

            if (mCurrentCustomer.getStatus().equals(CustomerStatus.STATUS_UNMODIFIED)) {
                mCurrentCustomer.withStatus(CustomerStatus.STATUS_MODIFIED);
            }

            requireNonNull(getSupportActionBar()).setTitle(R.string.add_customer_editing_title);
            requireNonNull(mInputLayoutSocialName.getEditText()).setText(mCurrentCustomer.getName());

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

            TypeOfPerson typeOfPerson = TypeOfPerson.valueOf(mCurrentCustomer.getType());
            if (typeOfPerson != null) {
                int position = mTypeOfPersonAdapter.getPosition(typeOfPerson);

                if (typeOfPerson.equals(TypeOfPerson.PRIVATE_INDIVIDUAL)) {
                    requireNonNull(mInputLayoutCpf.getEditText()).setText(mCurrentCustomer.getCpfOrCnpj());
                } else if (typeOfPerson.equals(TypeOfPerson.LEGAL_ENTITY)) {
                    requireNonNull(mInputLayoutCnpj.getEditText()).setText(mCurrentCustomer.getCpfOrCnpj());
                }

                if (position != -1) {
                    mSpinnerTypeOfPerson.setSelection(position + 1, true);
                }
            }
        } else {
            mCurrentCustomer = new Customer().withStatus(CustomerStatus.STATUS_CREATED);
            mIsEdition = false;
        }
    }

    private void loadStates() {
        if (mStateRepository == null) {
            mStateRepository = LocalDataInjector.provideStateRepository();
        }

        if (mStatesAdapter == null) {
            List<State> states = mStateRepository.findAll(StateEntity.Fields.NAME, true);
            showStates(states);
        }
    }

    private void showStates(List<State> states) {
        mSpinnerState.setAdapter(mStatesAdapter = new StatesAdapter(this, states));

        if (mCurrentCustomer.getCity() != null) {
            int position = mStatesAdapter.getPosition(mCurrentCustomer.getCity().getState());
            if (position != -1) {
                mSpinnerState.setSelection(position + 1, true);
            }
        }
    }

    private void executeSearchPostalCode(String value) {
        showProgressDialog(R.string.add_customer_searching_postal_code_message);

        Call<PostalCode> postalCode = RemoteDataInjector.providePostalCodeApi().get(value);
        postalCode.enqueue(new Callback<PostalCode>() {
            @Override
            public void onResponse(Call<PostalCode> call, Response<PostalCode> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showPostalCodeInfo(response.body());
                } else {
                    Timber.i("Server failure while syncing. %s", response.message());
                }
            }

            @Override
            public void onFailure(Call<PostalCode> call, Throwable t) {
                Timber.e(t, "Server failure while syncing.");
            }
        });
    }

    private void showPostalCodeInfo(PostalCode postalCode) {
        hideProgressDialog();
        requireNonNull(mInputLayoutDistrict.getEditText()).setText(postalCode.district);
        requireNonNull(mInputLayoutAddress.getEditText()).setText(postalCode.address);

        for (int i = 0; i < mStatesAdapter.getCount(); i++) {
            if (requireNonNull(mStatesAdapter.getItem(i)).getName().equalsIgnoreCase(postalCode.stateInfo.name)) {
                mSpinnerState.setSelection(i + 1, true);
                loadCityByName(postalCode.cityName);
            }
        }
    }

    private void loadCityByName(String name) {
        if (mCityRepository == null) {
            mCityRepository = LocalDataInjector.provideCityRepository();
        }
        executeLoadCityByName(name);
    }

    private void executeLoadCityByName(String name) {
        City city = mCityRepository.findFirst(new CityByNameSpecification(name));
        showCity(city);
    }

    private void showCity(final City city) {
        mCurrentCustomer.withCity(city);
        mInputLayoutCity.getEditText().setText(city.getName());
    }

    private void saveCustomer() {
        clearInputErrors();

        final boolean emptyRequiredInputs = checkEmptyRequiredInputs();
        final boolean validInputs = checkValidInputs();

        if (!emptyRequiredInputs && validInputs) {
            executeSaveCustomer();
        }
    }

    private void executeSaveCustomer() {
        if (mCustomerRepository == null) {
            mCustomerRepository = LocalDataInjector.provideCustomerRepository();
        }

        showProgressDialog(R.string.all_saving_message);

        if (mIsEdition) {
            saveCustomerInDataBase();
            showSuccessSavingCustomer();
        } else {
            saveCompanyCustomerInDataBase();
            showSuccessSavingCustomer();
        }
    }

    private void saveCustomerInDataBase() {
        mCustomerRepository.save(mCurrentCustomer);
    }

    private void saveCompanyCustomerInDataBase() {
        mCurrentCustomer = mCustomerRepository.save(mCurrentCustomer);
        saveCompanyCustomer(loadSelectedCompany(), mCurrentCustomer);
    }

    private Company loadSelectedCompany() {
        if (mLoggedUser == null) {
            mLoggedUser = eventBus().getStickyEvent(LoggedInUserEvent.class).getUser();
        }
        return mLoggedUser.getDefaultCompany();
    }

    private void saveCompanyCustomer(final Company company, final Customer customer) {
        if (mCompanyCustomerRepository == null) {
            mCompanyCustomerRepository = LocalDataInjector.provideCompanyCustomerRepository();
        }
        mCompanyCustomerRepository.save(CompanyCustomer.from(company, customer));
    }

    private void showSuccessSavingCustomer() {
        hideProgressDialog();
        Snackbar.make(mCoordinatorLayoutContainer, R.string.add_customer_saved_successfully, Snackbar.LENGTH_SHORT)
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(final Snackbar snackbar, final int event) {
                        eventBus().postSticky(SavedCustomerEvent.newEvent(mCurrentCustomer));
                        finish();
                    }
                })
                .show();
    }

    private void clearInputErrors() {
        // mSpinnerTypeOfPerson.setError(null);
        mInputLayoutSocialName.setError(null);
        mInputLayoutEmail.setError(null);
        // mSpinnerState.setError(null);
        mInputLayoutCity.setError(null);
        mInputLayoutPostalCode.setError(null);
        mInputLayoutMainPhone.setError(null);
        mInputLayoutOtherPhone.setError(null);
    }

    private boolean checkEmptyRequiredInputs() {
        final boolean emptyTypeOfPerson = checkEmptyRequiredInput(mSpinnerTypeOfPerson);
        final boolean emptySocialName = checkEmptyRequiredInput(mInputLayoutSocialName);
        final boolean emptyState = checkEmptyRequiredInput(mSpinnerState);
        final boolean emptyCity = checkEmptyRequiredInput(mInputLayoutCity);

        return emptyTypeOfPerson || emptySocialName || emptyState || emptyCity;
    }

    private boolean checkEmptyRequiredInput(Spinner spinner) {
        // spinner.setError(getString(R.string.all_required_field_error));
        return spinner.getSelectedItem() == null;
    }

    private boolean checkEmptyRequiredInput(TextInputLayout inputLayout) {
        if (isEmpty(inputLayout.getEditText().getText())) {
            inputLayout.setError(getString(R.string.all_required_field_error));
            return true;
        }
        return false;
    }

    private boolean checkValidInputs() {
        final boolean validCpfOrCnpj = checkValidCpfOrCnpjInput();
        final boolean validEmail = checkValidEmailInput();
        final boolean validPostalCode = checkValidPostalCodeInput();
        final boolean validMainPhone = checkValidMainPhoneInput();
        final boolean validOtherPhone = checkValidOtherPhoneInput();

        return validCpfOrCnpj && validEmail && validPostalCode && validMainPhone && validOtherPhone;
    }

    private boolean checkValidCpfOrCnpjInput() {
        String cpfOrCnpj = Mask.unmask(mCurrentCustomer.getCpfOrCnpj());

        if ((mCurrentCustomer.getType() != null) && !isEmpty(cpfOrCnpj)) {
            TypeOfPerson typeOfPerson = mTypeOfPersonAdapter.getItem(mSpinnerTypeOfPerson.getSelectedItemPosition());

            if (typeOfPerson.equals(TypeOfPerson.PRIVATE_INDIVIDUAL) && (cpfOrCnpj.length() != 11)) {
                mInputLayoutCpf.setError(getString(R.string.add_customer_invalid_cpf));
                return false;
            } else if (typeOfPerson.equals(TypeOfPerson.LEGAL_ENTITY) && (cpfOrCnpj.length() != 14)) {
                mInputLayoutCnpj.setError(getString(R.string.add_customer_invalid_cnpj));
                return false;
            }
        }
        return true;
    }

    private boolean checkValidEmailInput() {
        if (!isEmpty(mCurrentCustomer.getEmail())) {
            if (!ValidationUtils.isValidEmail(mCurrentCustomer.getEmail())) {
                mInputLayoutEmail.setError(getString(R.string.add_customer_invalid_email));
                return false;
            }
        }
        return true;
    }

    private boolean checkValidPostalCodeInput() {
        if (!isEmpty(mCurrentCustomer.getPostalCode())) {
            if (!ValidationUtils.isValidPostalCode(Mask.unmask(mCurrentCustomer.getPostalCode()))) {
                mInputLayoutPostalCode.setError(getString(R.string.add_customer_invalid_postal_code));
                return false;
            }
        }
        return true;
    }

    private boolean checkValidMainPhoneInput() {
        if (!isEmpty(mCurrentCustomer.getMainPhone())) {
            if (!ValidationUtils.isValidPhoneNumber(mCurrentCustomer.getMainPhone())) {
                mInputLayoutMainPhone.setError(getString(R.string.add_customer_invalid_phone_number));
                return false;
            }
        }
        return true;
    }

    private boolean checkValidOtherPhoneInput() {
        if (!isEmpty(mCurrentCustomer.getSecondaryPhone())) {
            if (!ValidationUtils.isValidPhoneNumber(mCurrentCustomer.getSecondaryPhone())) {
                mInputLayoutOtherPhone.setError(getString(R.string.add_customer_invalid_phone_number));
                return false;
            }
        }
        return true;
    }

    private void selectState(int position) {
        final State stateSelected = mStatesAdapter.getItem(position);
        if (stateSelected != null) {
            eventBus().postSticky(SelectedStateEvent.newEvent(stateSelected));
            navigate().toCityList();
        }
    }

    private void showProgressDialog(int titleRes) {
        mProgressDialog = new MaterialDialog.Builder(this)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .title(titleRes)
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
    protected void onDestroy() {
        eventBus().removeStickyEvent(SelectedCustomerEvent.class);
        super.onDestroy();
    }
}
