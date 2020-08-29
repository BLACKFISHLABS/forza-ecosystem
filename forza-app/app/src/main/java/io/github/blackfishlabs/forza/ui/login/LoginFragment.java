package io.github.blackfishlabs.forza.ui.login;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.RemoteDataInjector;
import io.github.blackfishlabs.forza.domain.dto.SalesmanDto;
import io.github.blackfishlabs.forza.domain.pojo.Company;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.Salesman;
import io.github.blackfishlabs.forza.ui.base.BaseFragment;
import io.github.blackfishlabs.forza.ui.exception.ValidationError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

import static java.util.Objects.requireNonNull;

public class LoginFragment extends BaseFragment {

    private static final int ONE_PROFILE = 1;

    public static final String TAG = LoginFragment.class.getName();

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private MaterialDialog mProgressDialog;

    @BindView(R.id.input_layout_login_user_identity)
    TextInputLayout mInputLayoutUserIdentity;

    @BindView(R.id.input_layout_login_user_password)
    TextInputLayout mInputLayoutUserPassword;

    @Override
    protected int provideContentViewResource() {
        return R.layout.fragment_login;
    }

    @OnClick(R.id.button_login)
    void onButtonLoginClicked() {
        login();
    }

    @OnEditorAction(R.id.edit_text_login_user_password)
    boolean onEditTextPasswordAction(KeyEvent key) {
        if (key == null || key.getAction() == KeyEvent.ACTION_UP) {
            login();
        }
        return true;
    }

    private void login() {
        if (!connectivity().isOnline()) {
            new MaterialDialog.Builder(requireNonNull(getContext()))
                    .title(R.string.all_offline_message_title)
                    .content(R.string.all_offline_message)
                    .positiveText(R.string.all_retry)
                    .onPositive((dialog, which) -> login())
                    .negativeText(android.R.string.cancel)
                    .onNegative((dialog, which) -> requireNonNull(getActivity()).finish())
                    .neutralText(android.R.string.ok)
                    .show();
            return;
        }

        boolean identityIsSet = userIdentityIsSet();
        boolean passwordIsSet = userPasswordIsSet();
        if (identityIsSet && passwordIsSet) {
            subscribeLogin();
        }
    }

    private void subscribeLogin() {
        startLogin();
        getSalesman();
    }

    private void processLoginError(final Throwable e) {
        stopLogin();
        Timber.e(e, "Could not do login");

        MaterialDialog.Builder builder = new MaterialDialog.Builder(requireNonNull(getContext())).neutralText(android.R.string.ok);

        if (e instanceof ValidationError) {
            builder.content(requireNonNull(e.getMessage())).show();
            return;
        }

        if (e instanceof HttpException) {
            builder.content(R.string.all_server_error_message + ": " + e.getMessage());
        } else if (e instanceof IOException) {
            builder.content(R.string.all_network_error_message + ": " + e.getMessage());
        } else {
            builder.content(R.string.all_unknown_error_message + ": " + e.getMessage());
        }

        builder
                .positiveText(R.string.all_retry)
                .onPositive((dialog, which) -> login())
                .negativeText(android.R.string.cancel)
                .onNegative((dialog, which) -> requireNonNull(getActivity()).finish())
                .show();
    }

    private void processLoginResult(Salesman salesman) {
        stopLogin();

        final List<Company> companies = salesman.getCompanies();
        if (companies.size() > ONE_PROFILE) {
            new MaterialDialog.Builder(requireNonNull(getContext()))
                    .title(R.string.login_select_default_company)
                    .items(companies)
                    .itemsCallbackSingleChoice(-1, (dialog, itemView, which, text) -> {
                        saveLogin(salesman, companies.get(which), companies.size());
                        return true;
                    })
                    .alwaysCallSingleChoiceCallback()
                    .show();
            return;
        }

        saveLogin(salesman, companies.get(0), ONE_PROFILE);
    }

    private void saveLogin(Salesman salesman, Company defaultCompany, int numberOfProfiles) {
        settings().setLoggedUser(salesman, defaultCompany);
        eventBus().postSticky(CompletedLoginEvent.newEvent(LoggedUser.create(salesman, defaultCompany)));
    }

    private void startLogin() {
        mProgressDialog = new MaterialDialog.Builder(requireNonNull(getContext()))
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .title(R.string.login_working_progress_message)
                .content(R.string.all_please_wait)
                .cancelable(true)
                .canceledOnTouchOutside(false)
                .cancelListener(cancelLogin())
                .show();
    }

    private DialogInterface.OnCancelListener cancelLogin() {
        return dialog -> stopLogin();
    }

    private void stopLogin() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private void getSalesman() {
        Call<SalesmanDto> salesmanDto = RemoteDataInjector.provideSalesmanApi().get(inputValue(mInputLayoutUserIdentity), inputValue(mInputLayoutUserPassword));
        salesmanDto.enqueue(new Callback<SalesmanDto>() {
            @Override
            public void onResponse(Call<SalesmanDto> call, Response<SalesmanDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().error) {
                        processLoginError(ValidationError.newError(response.body().message));
                    } else if (response.body().salesman.getCompanies().isEmpty()) {
                        processLoginError(ValidationError.newError(getString(R.string.login_salesman_with_no_companies_error)));
                    } else {
                        processLoginResult(response.body().salesman);
                    }
                } else {
                    Timber.i("Server failure while syncing. %s", response.message());
                }
            }

            @Override
            public void onFailure(Call<SalesmanDto> call, Throwable t) {
                processLoginError(t);
            }
        });
    }

    private boolean userIdentityIsSet() {
        return inputFieldIsSet(mInputLayoutUserIdentity, R.string.login_user_identity_not_set_error);
    }

    private boolean userPasswordIsSet() {
        return inputFieldIsSet(mInputLayoutUserPassword, R.string.login_user_password_not_set_error);
    }

    private boolean inputFieldIsSet(TextInputLayout inputLayout, @StringRes int errorCaseNotSet) {
        if (TextUtils.isEmpty(inputValue(inputLayout))) {
            inputLayout.setError(getString(errorCaseNotSet));
            return false;
        } else {
            inputLayout.setError(null);
            return true;
        }
    }

    private String inputValue(TextInputLayout input) {
        return requireNonNull(input.getEditText()).getText().toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
