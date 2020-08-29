package io.github.blackfishlabs.forza.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.TextDrawable.IShapeBuilder;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import io.github.blackfishlabs.forza.BuildConfig;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.company.customer.CompanyCustomerRepository;
import io.github.blackfishlabs.forza.data.company.pricetable.CompanyPriceTableRepository;
import io.github.blackfishlabs.forza.data.customer.CustomerRepository;
import io.github.blackfishlabs.forza.data.order.OrderItemRepository;
import io.github.blackfishlabs.forza.data.order.OrderRepository;
import io.github.blackfishlabs.forza.data.pricetable.PriceTableRepository;
import io.github.blackfishlabs.forza.data.product.ProductRepository;
import io.github.blackfishlabs.forza.data.settings.SettingsRepository;
import io.github.blackfishlabs.forza.domain.entity.PriceTableEntity;
import io.github.blackfishlabs.forza.domain.pojo.Company;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.PriceTable;
import io.github.blackfishlabs.forza.helper.AndroidUtils;
import io.github.blackfishlabs.forza.ui.PresentationInjector;
import io.github.blackfishlabs.forza.ui.base.BaseActivity;
import io.github.blackfishlabs.forza.ui.base.Navigator;
import io.github.blackfishlabs.forza.ui.customerlist.CustomerListFragment;
import io.github.blackfishlabs.forza.ui.dashboard.DashboardFragment;
import io.github.blackfishlabs.forza.ui.login.CompletedLoginEvent;
import io.github.blackfishlabs.forza.ui.orderlist.OrderListPageFragment;
import io.github.blackfishlabs.forza.ui.orderlist.SelectedOrderEvent;
import io.github.blackfishlabs.forza.ui.ordersreport.OrdersReportFragment;
import io.github.blackfishlabs.forza.ui.productlist.ProductListFragment;

import static android.text.TextUtils.isEmpty;
import static java.util.Objects.requireNonNull;
import static org.joda.time.LocalDateTime.parse;
import static org.joda.time.format.DateTimeFormat.shortDateTime;

public class MainActivity extends BaseActivity implements Drawer.OnDrawerItemClickListener {

    private static final int DRAWER_ITEM_DASHBOARD = 1;
    private static final int DRAWER_ITEM_ORDERS_REPORT = 2;
    private static final int DRAWER_ITEM_ORDERS = 3;
    private static final int DRAWER_ITEM_CUSTOMERS = 4;
    private static final int DRAWER_ITEM_PRODUCTS = 5;
    private static final int DRAWER_ITEM_AUTO_SYNC_ORDERS = 6;
    private static final int DRAWER_ITEM_SETTINGS = 7;
    private static final int DRAWER_ITEM_INFO = 9;
    private static final int DRAWER_ITEM_EXIT = 10;
    private static final int DRAWER_ITEM_ABOUT = 11;

    private Drawer drawer;
    private AccountHeader accountHeader;

    private PrimaryDrawerItem dashboardDrawerItem;
    private PrimaryDrawerItem ordersReportDrawerItem;
    private PrimaryDrawerItem ordersDrawerItem;
    private PrimaryDrawerItem customersDrawerItem;
    private PrimaryDrawerItem productsDrawerItem;
    private PrimaryDrawerItem settingsDrawerItem;
    private PrimaryDrawerItem aboutDrawerItem;

    private SecondarySwitchDrawerItem autoSyncOrdersDrawerItem;
    private SecondaryDrawerItem infoDrawerItem;
    private SecondaryDrawerItem exitDrawerItem;

    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior<View> bottomSheetBehavior;

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
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle inState) {
        navigateToInitialFlowIfNeed();
        super.onCreate(inState);
        setAsHomeActivity();
        initBottomSheet();
        initDrawerHeader(inState);
        initDrawer(inState);

        mOrderRepository = LocalDataInjector.providerOrderRepository();
        mOrderItemRepository = LocalDataInjector.providerOrderItemRepository();
        mProductRepository = LocalDataInjector.provideProductRepository();

        mCustomerRepository = LocalDataInjector.provideCustomerRepository();
        mCompanyCustomerRepository = LocalDataInjector.provideCompanyCustomerRepository();
        mPriceTableRepository = LocalDataInjector.providePriceTableRepository();
        mCompanyPriceTableRepository = LocalDataInjector.provideCompanyPriceTableRepository();

        settingsRepository = PresentationInjector.provideSettingsRepository();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (settings().isUserLoggedIn()) {
            loadLoggedUser();
        }
    }

    @Override
    public boolean onItemClick(
            final View view, final int position, final IDrawerItem drawerItem) {
        switch ((int) drawerItem.getIdentifier()) {
            case DRAWER_ITEM_DASHBOARD: {
                if (!isViewingDashboard()) {
                    navigateToDashboard();
                }
                break;
            }
            case DRAWER_ITEM_ORDERS_REPORT: {
                if (!isViewingOrdersReport()) {
                    navigateToOrdersReport();
                }
                break;
            }
            case DRAWER_ITEM_ORDERS: {
                if (!isViewingOrderList()) {
                    navigateToOrderList();
                }
                break;
            }
            case DRAWER_ITEM_CUSTOMERS: {
                if (!isViewingCustomerList()) {
                    navigateToCustomerList();
                }
                break;
            }
            case DRAWER_ITEM_PRODUCTS: {
                if (!isViewingProductList()) {
                    navigateToProductList();
                }
                break;
            }
            case DRAWER_ITEM_SETTINGS: {
                navigate().toSettings();
                break;
            }
            case DRAWER_ITEM_ABOUT: {
                if (!isViewingAbout()) {
                    navigate().toAbout();
                }
                break;
            }
            case DRAWER_ITEM_EXIT: {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Antes de continuar!");
                alert.setMessage("Esta opção limpa sua base local sincronizada e limpa seu login da aplicação!");
                alert.setCancelable(false);
                alert
                        .setNegativeButton("voltar", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("OK! Quero fazer isso!", (dialogInterface, i) -> clearAllData());

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                break;
            }
        }
        return false;
    }

    private void clearAllData() {
        Toast.makeText(getApplicationContext(), "Bye :)", Toast.LENGTH_LONG).show();

        mOrderRepository.findAllAndDelete();
        mOrderItemRepository.findAllAndDelete();
        mProductRepository.findAllAndDelete();

        mCustomerRepository.findAllAndDelete();
        mCompanyCustomerRepository.findAllAndDelete();
        mPriceTableRepository.findAllAndDelete();
        mCompanyPriceTableRepository.findAllAndDelete();

        settingsRepository.setChargeCode("");
        settingsRepository.clearAllSettings();

        this.finishAffinity();
    }

    @OnClick(R.id.fab_all_main_action)
    void onFabClicked() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (drawer != null) {
            outState = drawer.saveInstanceState(outState);
        }
        if (accountHeader != null) {
            outState = accountHeader.saveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(
            final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case Navigator.REQUEST_INITIAL_FLOW: {
                if (resultCode == RESULT_OK) {
                    loadLoggedUser();
                } else {
                    finish();
                }
                break;
            }
            case Navigator.REQUEST_SETTINGS: {
                break;
            }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void navigateToInitialFlowIfNeed() {
        if (!settings().isInitialFlowDone()) {
            navigate().toInitialFlow();
        }
    }

    private void initBottomSheet() {
        final View bottomSheetView = getLayoutInflater().inflate(R.layout.view_bottom_sheet, null);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setOnDismissListener(dialog -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());

        bottomSheetView.findViewById(R.id.main_new_order)
                .setOnClickListener(v -> {
                    List<PriceTable> priceTableExists = LocalDataInjector
                            .providePriceTableRepository()
                            .findAll(PriceTableEntity.Fields.PRICE_TABLE_ID, true);

                    if (priceTableExists.isEmpty()) {
                        Toast.makeText(this, "Para realizar um pedido é necessário importar seus produtos e clientes.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    eventBus().postSticky(SelectedOrderEvent.selectOrder(null));
                    navigate().toAddOrder();
                    bottomSheetDialog.dismiss();
                });
//        bottomSheetView.findViewById(R.id.main_new_customer)
//                .setOnClickListener(v -> {
//                    navigate().toAddCustomer();
//                    bottomSheetDialog.dismiss();
//                });
        bottomSheetView.findViewById(R.id.main_new_resume)
                .setOnClickListener(v -> {
                    navigate().toImportResume();
                    bottomSheetDialog.dismiss();
                });
    }

    private void initDrawerHeader(final Bundle inState) {
        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withSavedInstance(inState)
                .withOnlyMainProfileImageVisible(true)
                .withCurrentProfileHiddenInList(true)
                .withSelectionListEnabledForSingleProfile(false)
                .withCompactStyle(true)
                .withOnAccountHeaderListener((view, profile, current) -> {
                    changeLoggedUser(profile);
                    return false;
                })
                .build();
    }

    private void initDrawer(final Bundle inState) {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(requireNonNull(mToolbar))
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        createDashboardDrawerItem(),
                        createOrdersDrawerItem(),
                        createCustomersDrawerItem(),
                        createProductsDrawerItem(),
                        createOrdersReportDrawerItem(),
                        createSettingsDrawerItem(),
                        createAboutDrawerItem(),
                        new DividerDrawerItem(),
                        createAutoSyncOrdersDrawerItem(),
                        createInfoDrawerItem(),
                        createExitDrawerItem()
                )
                .withSavedInstance(inState)
                .withShowDrawerOnFirstLaunch(true)
                .withOnDrawerItemClickListener(this)
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(final View drawerView) {
                        updateAutoSyncOrdersDrawerItem();
                        updateLastSyncDrawerItem();
                    }

                    @Override
                    public void onDrawerClosed(final View drawerView) {
                    }

                    @Override
                    public void onDrawerSlide(final View drawerView,
                                              final float slideOffset) {
                    }
                })
                .withSelectedItem(DRAWER_ITEM_DASHBOARD)
                .withFireOnInitialOnClick(true)
                .build();
    }

    private PrimaryDrawerItem createDashboardDrawerItem() {
        if (dashboardDrawerItem == null) {
            dashboardDrawerItem = new PrimaryDrawerItem()
                    .withIdentifier(DRAWER_ITEM_DASHBOARD)
                    .withName(R.string.main_drawer_item_dashboard)
                    .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_dashboard, getTheme()))
                    .withSelectedIconColorRes(R.color.color_primary_dark);
        }
        return dashboardDrawerItem;
    }

    private PrimaryDrawerItem createOrdersReportDrawerItem() {
        if (ordersReportDrawerItem == null) {
            ordersReportDrawerItem = new PrimaryDrawerItem()
                    .withIdentifier(DRAWER_ITEM_ORDERS_REPORT)
                    .withName(R.string.main_drawer_item_orders_report)
                    .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_content_copy, getTheme()))
                    .withSelectedIconColorRes(R.color.color_primary_dark);
        }
        return ordersReportDrawerItem;
    }

    private PrimaryDrawerItem createOrdersDrawerItem() {
        if (ordersDrawerItem == null) {
            ordersDrawerItem = new PrimaryDrawerItem()
                    .withIdentifier(DRAWER_ITEM_ORDERS)
                    .withName(R.string.main_drawer_item_orders)
                    .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_content_paste, getTheme()))
                    .withSelectedIconColorRes(R.color.color_primary_dark);
        }
        return ordersDrawerItem;
    }

    private PrimaryDrawerItem createCustomersDrawerItem() {
        if (customersDrawerItem == null) {
            customersDrawerItem = new PrimaryDrawerItem()
                    .withIdentifier(DRAWER_ITEM_CUSTOMERS)
                    .withName(R.string.main_drawer_item_customers)
                    .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_person_outline, getTheme()))
                    .withSelectedIconColorRes(R.color.color_primary_dark);
        }
        return customersDrawerItem;
    }

    private PrimaryDrawerItem createProductsDrawerItem() {
        if (productsDrawerItem == null) {
            productsDrawerItem = new PrimaryDrawerItem()
                    .withIdentifier(DRAWER_ITEM_PRODUCTS)
                    .withName(R.string.main_drawer_item_products)
                    .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_local_offer, getTheme()))
                    .withSelectedIconColorRes(R.color.color_primary_dark);
        }
        return productsDrawerItem;
    }

    private PrimaryDrawerItem createSettingsDrawerItem() {
        if (settingsDrawerItem == null) {
            settingsDrawerItem = new PrimaryDrawerItem()
                    .withIdentifier(DRAWER_ITEM_SETTINGS)
                    .withName(R.string.main_drawer_item_settings)
                    .withDescription("Sincronização")
                    .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_settings, getTheme()))
                    .withSelectable(false);
        }
        return settingsDrawerItem;
    }

    private PrimaryDrawerItem createAboutDrawerItem() {
        if (aboutDrawerItem == null) {
            aboutDrawerItem = new PrimaryDrawerItem()
                    .withIdentifier(DRAWER_ITEM_ABOUT)
                    .withName(R.string.main_drawer_item_about)
                    .withDescription("Sobre")
                    .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_code, getTheme()));
        }

        return aboutDrawerItem;
    }

    private SecondarySwitchDrawerItem createAutoSyncOrdersDrawerItem() {
        if (autoSyncOrdersDrawerItem == null) {
            autoSyncOrdersDrawerItem = new SecondarySwitchDrawerItem()
                    .withIdentifier(DRAWER_ITEM_AUTO_SYNC_ORDERS)
                    .withName(R.string.main_drawer_item_auto_sync_orders)
                    .withDescription(getLastSyncDrawerItemDescription())
                    .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_sync, getTheme()))
                    .withSelectedIconColorRes(R.color.color_primary_dark)
                    .withChecked(settings().getSettings().isAutomaticallySyncOrders())
                    .withSelectable(false)
                    .withOnCheckedChangeListener((drawerItem, buttonView, isChecked) ->
                            settings().setAutoSyncOrders(isChecked));
        }
        return autoSyncOrdersDrawerItem;
    }

    private SecondaryDrawerItem createInfoDrawerItem() {
        if (infoDrawerItem == null) {
            infoDrawerItem = new SecondaryDrawerItem()
                    .withIdentifier(DRAWER_ITEM_INFO)
                    .withIcon(VectorDrawableCompat
                            .create(getResources(), R.drawable.ic_perm_device_information, getTheme()))
                    .withSelectedIconColorRes(R.color.color_primary_dark)
                    .withName("Versão do Aplicativo")
                    .withDescription(BuildConfig.VERSION_NAME)
                    .withSelectable(false);
        }
        return infoDrawerItem;
    }

    private SecondaryDrawerItem createExitDrawerItem() {
        if (exitDrawerItem == null) {
            exitDrawerItem = new SecondaryDrawerItem()
                    .withIdentifier(DRAWER_ITEM_EXIT)
                    .withIcon(VectorDrawableCompat
                            .create(getResources(), R.drawable.ic_exit_to_app, getTheme()))
                    .withSelectedIconColorRes(R.color.color_primary_dark)
                    .withName("Logout")
                    .withDescription("Remove Acesso e Limpa Base Local")
                    .withSelectable(false);
        }
        return exitDrawerItem;
    }

    private void updateLastSyncDrawerItem() {
        String lastSyncDrawerItemDescription = getLastSyncDrawerItemDescription();
        if (!autoSyncOrdersDrawerItem.getDescription().toString()
                .equals(lastSyncDrawerItemDescription)) {
            drawer.updateItem(autoSyncOrdersDrawerItem.withDescription(lastSyncDrawerItemDescription));
        }
    }

    private String getLastSyncDrawerItemDescription() {
        final String lastSyncTime = settings().getLastSyncTime();
        if (!isEmpty(lastSyncTime)) {
            return getString(R.string.main_drawer_item_desc_last_sync,
                    shortDateTime().print(parse(lastSyncTime)));
        } else {
            return "";
        }
    }

    private void updateAutoSyncOrdersDrawerItem() {
        boolean isEnabled = settings().getSettings().isAutomaticallySyncOrders();
        if (isEnabled != autoSyncOrdersDrawerItem.isChecked()) {
            drawer.updateItem(autoSyncOrdersDrawerItem.withChecked(isEnabled));
        }
    }

    private void loadLoggedUser() {
        CompletedLoginEvent completedLoginEvent
                = eventBus().getStickyEvent(CompletedLoginEvent.class);

        if (completedLoginEvent != null) {
            eventBus().removeStickyEvent(CompletedLoginEvent.class);
            showLoggedUser(completedLoginEvent.getUser());
        } else {
            showLoggedUser(settings().getLoggedUser());
        }
    }

    private void showLoggedUser(final LoggedUser loggedUser) {
        eventBus().postSticky(LoggedInUserEvent.logged(loggedUser));
        createProfiles(loggedUser);
    }

    private void createProfiles(final LoggedUser loggedUser) {
        IShapeBuilder builder = createShapeBuilder();

        List<IProfile> profiles = new ArrayList<>();
        IProfile activeProfile = null;
        for (Company company : loggedUser.getSalesman().getCompanies()) {
            final String name = generateNameWithTwoLetters(company.getName());

            ProfileDrawerItem profile = new ProfileDrawerItem()
                    .withName(loggedUser.getSalesman().getName())
                    .withEmail(company.getName())
                    .withIcon(builder.buildRound(name, Color.DKGRAY))
                    .withTag(company)
                    .withSetSelected(company.equals(loggedUser.getDefaultCompany()));

            if (profile.isSelected()) {
                activeProfile = profile;
            }

            profiles.add(profile);
        }

        setProfiles(profiles, activeProfile);
    }

    private IShapeBuilder createShapeBuilder() {
        return TextDrawable.builder()
                .beginConfig()
                .fontSize(AndroidUtils.dpToPx(this, 20))
                .toUpperCase()
                .endConfig();
    }

    private String generateNameWithTwoLetters(String name) {
        String[] parts = name.trim().split("\\s+");

        if (parts.length == 1) {
            return parts[0].substring(0, 2);
        } else {
            return parts[0].substring(0, 1).concat(parts[1].substring(0, 1));
        }
    }

    private void setProfiles(final List<IProfile> profiles, final IProfile activeProfile) {
        accountHeader.setProfiles(profiles);
        accountHeader.setActiveProfile(activeProfile);
    }

    private void changeLoggedUser(IProfile profile) {
        LoggedUser loggedUser = eventBus().getStickyEvent(LoggedInUserEvent.class).getUser();
        final Object tag = ((ProfileDrawerItem) profile).getTag();
        final Company company = (Company) tag;

        loggedUser = loggedUser.withDefaultCompany(company);
        settings().setLoggedUser(loggedUser.getSalesman(), company);
        eventBus().postSticky(LoggedInUserEvent.logged(loggedUser));
    }

    private boolean isViewingDashboard() {
        return isViewingFragmentByTag(DashboardFragment.TAG);
    }

    private void navigateToDashboard() {
        navigate().toDashboard();
    }

    private boolean isViewingOrdersReport() {
        return isViewingFragmentByTag(OrdersReportFragment.TAG);
    }

    private void navigateToOrdersReport() {
        navigate().toOrdersReport();
    }

    private boolean isViewingOrderList() {
        return isViewingFragmentByTag(OrderListPageFragment.TAG);
    }

    private void navigateToOrderList() {
        navigate().toOrderList();
    }

    private boolean isViewingCustomerList() {
        return isViewingFragmentByTag(CustomerListFragment.TAG);
    }

    private void navigateToCustomerList() {
        navigate().toCustomerList();
    }

    private boolean isViewingProductList() {
        return isViewingFragmentByTag(ProductListFragment.TAG);
    }

    private void navigateToProductList() {
        navigate().toProductList();
    }

    private boolean isViewingAbout() {
        return isViewingFragmentByTag(AboutFragment.TAG);
    }

    private boolean isViewingFragmentByTag(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment != null && fragment.isVisible();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
