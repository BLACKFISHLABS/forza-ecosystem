package io.github.blackfishlabs.forza.ui.importation;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnTextChanged;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.RemoteDataInjector;
import io.github.blackfishlabs.forza.data.company.customer.CompanyCustomerRepository;
import io.github.blackfishlabs.forza.data.company.pricetable.CompanyPriceTableRepository;
import io.github.blackfishlabs.forza.data.customer.CustomerRepository;
import io.github.blackfishlabs.forza.data.order.OrderItemRepository;
import io.github.blackfishlabs.forza.data.order.OrderRepository;
import io.github.blackfishlabs.forza.data.pricetable.PriceTableRepository;
import io.github.blackfishlabs.forza.data.product.ProductRepository;
import io.github.blackfishlabs.forza.data.settings.SettingsRepository;
import io.github.blackfishlabs.forza.domain.pojo.Company;
import io.github.blackfishlabs.forza.domain.pojo.CompanyCustomer;
import io.github.blackfishlabs.forza.domain.pojo.CompanyPriceTable;
import io.github.blackfishlabs.forza.domain.pojo.Customer;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.PriceTable;
import io.github.blackfishlabs.forza.ui.PresentationInjector;
import io.github.blackfishlabs.forza.ui.base.BaseActivity;
import io.github.blackfishlabs.forza.ui.login.CompletedLoginEvent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.requireNonNull;

public class ImportResumeActivity extends BaseActivity {

    @BindView(R.id.input_layout_resume_code)
    TextInputLayout mResumeCode;
    @BindView(R.id.text_view_code_resume)
    TextView mViewCode;
    @BindView(R.id.button_sync_resume)
    Button mSync;
    @BindView(R.id.button_finish_resume)
    Button mFinish;
    @BindView(R.id.button_cancel_resume)
    Button mCancel;

    private CustomerRepository mCustomerRepository;
    private CompanyCustomerRepository mCompanyCustomerRepository;
    private PriceTableRepository mPriceTableRepository;
    private CompanyPriceTableRepository mCompanyPriceTableRepository;
    private OrderRepository mOrderRepository;
    private OrderItemRepository mOrderItemRepository;
    private ProductRepository mProductRepository;
    private SettingsRepository settingsRepository;

    @Override
    protected int provideContentViewResource() {
        return R.layout.activity_import_resume;
    }

    @Override
    protected void onCreate(@Nullable Bundle inState) {
        super.onCreate(inState);
        setAsSubActivity();

        mSync.setEnabled(false);
        mSync.setOnClickListener(v -> searchAndImport());
        mCancel.setOnClickListener(v -> cancelResume());
        mFinish.setOnClickListener(v -> finishResume());

        mOrderRepository = LocalDataInjector.providerOrderRepository();
        mOrderItemRepository = LocalDataInjector.providerOrderItemRepository();
        mProductRepository = LocalDataInjector.provideProductRepository();

        mCustomerRepository = LocalDataInjector.provideCustomerRepository();
        mCompanyCustomerRepository = LocalDataInjector.provideCompanyCustomerRepository();
        mPriceTableRepository = LocalDataInjector.providePriceTableRepository();
        mCompanyPriceTableRepository = LocalDataInjector.provideCompanyPriceTableRepository();

        settingsRepository = PresentationInjector.provideSettingsRepository();
    }

    private void finishResume() {
        // Warning
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(getString(R.string.app_name));
        alert.setMessage("Deseja finalizar esta carga?");
        alert.setCancelable(false);
        alert
                .setNegativeButton("não", (dialogInterface, i) -> {
                })
                .setPositiveButton("sim", (dialogInterface, i) -> {
                    String code = requireNonNull(mResumeCode.getEditText()).getText().toString();
                    final LoggedUser loggedUser = settingsRepository.getLoggedUser();

                    String companyCnpj = loggedUser.getDefaultCompany().getCnpj();
                    String salesmanCpfOrCnpj = loggedUser.getSalesman().getCpfOrCnpj();

                    Call<ResponseBody> charge = RemoteDataInjector.provideSyncApi().getCharge(code.trim(), companyCnpj, salesmanCpfOrCnpj);
                    charge.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String json = "";
                                try {
                                    json = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                // Remove local data
                                clearDataResume();

                                // Sync Status
                                updateCharge(requireNonNull(json).replace("\"status\":1", "\"status\":2"));
                            } else {
                                Toast.makeText(getApplicationContext(), "Não foi localizado nenhuma carga!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Não foi localizado nenhuma carga!", Toast.LENGTH_LONG).show();
                        }
                    });
                });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isNullOrEmpty(settingsRepository.getSettings().getChargeCode())) {
            mSync.setVisibility(View.VISIBLE);
            mViewCode.setText(getString(R.string.no_resume_loaded));
            mResumeCode.setVisibility(View.VISIBLE);

            mCancel.setVisibility(View.GONE);
            mFinish.setVisibility(View.GONE);
        } else {
            mSync.setVisibility(View.GONE);
            mResumeCode.setVisibility(View.GONE);

            String info = "Carga: ".concat(requireNonNull(settingsRepository.getSettings().getChargeCode()));
            mViewCode.setText(info);

            mCancel.setVisibility(View.VISIBLE);
            mFinish.setVisibility(View.VISIBLE);
        }
    }

    private void cancelResume() {
        // Warning
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(getString(R.string.app_name));
        alert.setMessage("Deseja apagar todos os dados importados dessa carga?".toUpperCase());
        alert.setCancelable(false);
        alert
                .setNegativeButton("não", (dialogInterface, i) -> {
                })
                .setPositiveButton("sim", (dialogInterface, i) -> clearDataResume());

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void clearDataResume() {
        settingsRepository.setChargeCode("");

        mOrderRepository.findAllAndDelete();
        mOrderItemRepository.findAllAndDelete();
        mProductRepository.findAllAndDelete();

        mCustomerRepository.findAllAndDelete();
        mCompanyCustomerRepository.findAllAndDelete();
        mPriceTableRepository.findAllAndDelete();
        mCompanyPriceTableRepository.findAllAndDelete();

        requireNonNull(mResumeCode.getEditText()).setText("");

        Toast.makeText(getApplicationContext(), "Carga apagada do sistema!", Toast.LENGTH_LONG).show();
        onResume();
    }

    private void searchAndImport() {
        // Warning
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(getString(R.string.app_name));
        alert.setMessage("Deseja importar a carga digitada?");
        alert.setCancelable(false);
        alert
                .setNegativeButton("não", (dialogInterface, i) -> {
                })
                .setPositiveButton("sim", (dialogInterface, i) -> {
                    Toast.makeText(getApplicationContext(), "Aguarde a conclusão da importação...", Toast.LENGTH_LONG).show();
                    String code = requireNonNull(mResumeCode.getEditText()).getText().toString();
                    final LoggedUser loggedUser = settingsRepository.getLoggedUser();

                    String companyCnpj = loggedUser.getDefaultCompany().getCnpj();
                    String salesmanCpfOrCnpj = loggedUser.getSalesman().getCpfOrCnpj();

                    Call<ResponseBody> charge = RemoteDataInjector.provideSyncApi().getCharge(code.trim(), companyCnpj, salesmanCpfOrCnpj);
                    charge.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String json = null;
                                try {
                                    json = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Gson gson = new Gson();
                                Type empMapType = new TypeToken<Map<String, Object>>() {
                                }.getType();
                                Map<String, Object> map = gson.fromJson(json, empMapType);

                                String resCode = (String) requireNonNull(map).get("codigo");
                                String plate = (String) ((LinkedTreeMap) requireNonNull(map.get("veiculo"))).get("placa");
                                String info = "Carga Carregada: ".concat(requireNonNull(resCode)).concat("\n").concat("Placa: ".concat(requireNonNull(plate)));
                                Timber.i(info, response.message());

                                if (json.contains("\"status\":2")) {
                                    Toast.makeText(getApplicationContext(), "Esta carga já está finalizada!", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                // Adding local data
                                settingsRepository.setChargeCode(resCode);
                                loadLoggedUser();

                                // Sync Status
                                updateCharge(json.replace("\"status\":0", "\"status\":1"));
                            } else {
                                Toast.makeText(getApplicationContext(), "Não foi localizado nenhuma carga!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Não foi localizado nenhuma carga!", Toast.LENGTH_LONG).show();
                        }
                    });
                });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void updateCharge(String map) {
        RemoteDataInjector.provideSyncApi().updateCharge(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getApplicationContext(), "Carga sincronizada com sucesso", Toast.LENGTH_LONG).show();
                } else {
                    Timber.i("Server failure while syncing. %s", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Timber.e(t, "Server failure while syncing.");
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

        for (Company company : companies) {
            importForCompany(company);
        }

        onResume();
    }

    private void importForCompany(Company company) {
        importCustomers(company);
        importPriceTable(company);
    }

    private void importCustomers(final Company company) {
        Call<List<Customer>> customers = RemoteDataInjector.provideCustomerApi().getByCharge(settingsRepository.getSettings().getChargeCode(), company.getCnpj());
        customers.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mCustomerRepository.save(response.body());
                    saveCompanyCustomers(company, response.body());
                } else {
                    Timber.i("Server failure while syncing. %s", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Timber.e(t, "Server failure while syncing.");
            }
        });
    }

    private void saveCompanyCustomers(final Company company, final List<Customer> customers) {
        mCompanyCustomerRepository.save(toCompanyCustomers(company, customers));
    }

    private void importPriceTable(final Company company) {
        Call<List<PriceTable>> prices = RemoteDataInjector.providePriceTableApi().getByCharge(settingsRepository.getSettings().getChargeCode(), company.getCnpj());
        prices.enqueue(new Callback<List<PriceTable>>() {
            @Override
            public void onResponse(Call<List<PriceTable>> call, Response<List<PriceTable>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mPriceTableRepository.save(response.body());
                    saveCompanyPriceTables(company, response.body());
                } else {
                    Timber.i("Server failure while syncing. %s", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<PriceTable>> call, Throwable t) {
                Timber.e(t, "Server failure while syncing.");
            }
        });
    }

    private void saveCompanyPriceTables(final Company company, final List<PriceTable> priceTables) {
        mCompanyPriceTableRepository.save(toCompanyPriceTables(company, priceTables));
    }

    private List<CompanyCustomer> toCompanyCustomers(final Company company, final List<Customer> customers) {
        List<CompanyCustomer> companyCustomers = new ArrayList<>();

        for (Customer customer : customers) {
            companyCustomers.add(CompanyCustomer.from(company, customer));
        }
        return companyCustomers;
    }

    private List<CompanyPriceTable> toCompanyPriceTables(final Company company, final List<PriceTable> priceTables) {
        List<CompanyPriceTable> companyPriceTables = new ArrayList<>();

        for (PriceTable priceTable : priceTables) {
            companyPriceTables.add(CompanyPriceTable.from(company, priceTable));
        }
        return companyPriceTables;
    }

    @OnTextChanged(R.id.edit_text_resume_code)
    void onEditTextAddressChanged(CharSequence text) {
        mSync.setEnabled(text.length() > 1);
    }
}
