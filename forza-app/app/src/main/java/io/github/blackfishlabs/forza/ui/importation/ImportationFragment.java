package io.github.blackfishlabs.forza.ui.importation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.RemoteDataInjector;
import io.github.blackfishlabs.forza.data.city.CityRepository;
import io.github.blackfishlabs.forza.data.company.paymentmethod.CompanyPaymentMethodRepository;
import io.github.blackfishlabs.forza.data.paymentmethod.PaymentMethodRepository;
import io.github.blackfishlabs.forza.data.sync.SyncTaskService;
import io.github.blackfishlabs.forza.domain.dto.ServerStatus;
import io.github.blackfishlabs.forza.domain.pojo.City;
import io.github.blackfishlabs.forza.domain.pojo.Company;
import io.github.blackfishlabs.forza.domain.pojo.CompanyPaymentMethod;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.PaymentMethod;
import io.github.blackfishlabs.forza.ui.base.BaseFragment;
import io.github.blackfishlabs.forza.ui.login.CompletedLoginEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

import static java.util.Objects.requireNonNull;

public class ImportationFragment extends BaseFragment {

    public static final String TAG = ImportationFragment.class.getName();

    private ImportationAdapter mRecyclerViewAdapter;
    private CityRepository mCityRepository;
    private PaymentMethodRepository mPaymentMethodRepository;
    private CompanyPaymentMethodRepository mCompanyPaymentMethodRepository;
    private AtomicInteger mImportationTotalCounter = new AtomicInteger(1);
    private AtomicInteger mImportationCompletedCounter = new AtomicInteger();
    private MaterialDialog progressDialog;

    @BindView(R.id.recycler_view_importation_companies)
    RecyclerView mRecyclerView;

    @BindView(R.id.button_all_retry)
    Button mButtonRetryImportStatesAndCities;

    public static ImportationFragment newInstance() {
        return new ImportationFragment();
    }

    @Override
    protected int provideContentViewResource() {
        return R.layout.fragment_importation;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle inState) {
        View view = super.onCreateView(inflater, container, inState);

        mRecyclerView.setHasFixedSize(true);

        mCityRepository = LocalDataInjector.provideCityRepository();
        mPaymentMethodRepository = LocalDataInjector.providePaymentMethodRepository();
        mCompanyPaymentMethodRepository = LocalDataInjector.provideCompanyPaymentMethodRepository();

        eventBus().register(this);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        loadLoggedUser();
    }

    @Subscribe
    public void onRetryImport(RetryImportEvent event) {
        importForCompany(event.getCompany());
    }

    @OnClick(R.id.button_all_retry)
    void onButtonRetryImportStatesAndCitiesClicked() {
        importCities();
    }

    private void importCities() {
        mButtonRetryImportStatesAndCities.setVisibility(View.GONE);

        Call<List<City>> cities = RemoteDataInjector.provideCityApi().get();
        cities.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mCityRepository.save(response.body());
                    incrementAndCheckImportantionCounter();
                } else {
                    Timber.i("Server failure while syncing. %s", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Timber.e(t, "Server failure while syncing.");
                mButtonRetryImportStatesAndCities.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadLoggedUser() {
        CompletedLoginEvent completedLoginEvent = eventBus().getStickyEvent(CompletedLoginEvent.class);

        if (completedLoginEvent != null) {
            showCompaniesFromLoggedUser(completedLoginEvent.getUser());
            return;
        }

        showCompaniesFromLoggedUser(settings().getLoggedUser());
    }

    private void showCompaniesFromLoggedUser(final LoggedUser loggedUser) {
        final List<Company> companies = loggedUser.getSalesman().getCompanies();

        mRecyclerViewAdapter = new ImportationAdapter(companies);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        if (mImportationTotalCounter.get() == 1) {
            mImportationTotalCounter.addAndGet(companies.size());
        }

        for (Company company : companies) {
            importForCompany(company);
        }
    }

    private void importForCompany(Company company) {
        mRecyclerViewAdapter.updateProgress(company, 1);
        importCities();
        importPaymentMethods(company);
        //This imported in resume, don't change this
        mRecyclerViewAdapter.updateProgress(company, 2);
        incrementAndCheckImportantionCounter();
    }

    private void importPaymentMethods(final Company company) {
        Call<List<PaymentMethod>> payments = RemoteDataInjector.providePaymentMethodApi().get(company.getCnpj());
        payments.enqueue(new Callback<List<PaymentMethod>>() {
            @Override
            public void onResponse(Call<List<PaymentMethod>> call, Response<List<PaymentMethod>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mPaymentMethodRepository.save(response.body());
                    saveCompanyPaymentMethods(company, response.body());
                } else {
                    Timber.i("Server failure while syncing. %s", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PaymentMethod>> call, Throwable t) {
                Timber.e(t, "Server failure while syncing.");
                mRecyclerViewAdapter.updateProgress(company, 0);
            }
        });
    }

    private void saveCompanyPaymentMethods(final Company company, final List<PaymentMethod> paymentMethods) {
        mCompanyPaymentMethodRepository.save(toCompanyPaymentMethods(company, paymentMethods));
    }

    private List<CompanyPaymentMethod> toCompanyPaymentMethods(final Company company, final List<PaymentMethod> paymentMethods) {
        List<CompanyPaymentMethod> companyPaymentMethods = new ArrayList<>();

        for (PaymentMethod paymentMethod : paymentMethods) {
            companyPaymentMethods.add(CompanyPaymentMethod.from(company, paymentMethod));
        }
        return companyPaymentMethods;
    }

    private void incrementAndCheckImportantionCounter() {
        if (mImportationCompletedCounter.incrementAndGet() == mImportationTotalCounter.get()) {
            getServerStatus();
        }
    }

    private void getServerStatus() {
        showProgressDialog();
        Call<ServerStatus> serverStatus = RemoteDataInjector.provideSyncApi().serverStatus();
        serverStatus.enqueue(new Callback<ServerStatus>() {
            @Override
            public void onResponse(Call<ServerStatus> call, Response<ServerStatus> response) {
                if (response.isSuccessful() && response.body() != null) {
                    settings().setLastSyncTime(response.body().currentTime);
                    completeInitialFlowDone();
                } else {
                    Timber.i("Server failure while syncing. %s", response.message());
                }

            }

            @Override
            public void onFailure(Call<ServerStatus> call, Throwable t) {
                Timber.e(t, "Server failure while syncing.");
                handleServerStatusError(t);
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new MaterialDialog.Builder(requireNonNull(getContext()))
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .title(R.string.importation_getting_server_status)
                .content(R.string.all_please_wait)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void handleServerStatusError(Throwable e) {
        hideProgressDialog();
        Timber.e(e, "Could not get server status");

        MaterialDialog.Builder builder = new MaterialDialog.Builder(requireNonNull(getContext())).neutralText(android.R.string.ok);

        if (e instanceof HttpException) {
            builder.content(R.string.all_server_error_message);
        } else if (e instanceof IOException) {
            builder.content(R.string.all_network_error_message);
        } else {
            builder.content(R.string.all_unknown_error_message);
        }

        builder
                .positiveText(R.string.all_retry)
                .onPositive((dialog, which) -> getServerStatus())
                .negativeText(android.R.string.cancel)
                .onNegative((dialog, which) -> requireNonNull(getActivity()).finish())
                .show();
    }

    private void completeInitialFlowDone() {
        hideProgressDialog();
        settings().setInitialFlowDone();
        SyncTaskService.schedule(requireNonNull(getContext()), settings().getSettings().getSyncPeriodicity());
        new MaterialDialog.Builder(getContext())
                .title(R.string.importation_completed)
                .content(R.string.importation_completed_message)
                .positiveText(android.R.string.ok)
                .onPositive((dialog, which) ->
                        eventBus().post(CompletedImportationEvent.newEvent()))
                .show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        eventBus().unregister(this);
        super.onDestroyView();
    }
}
