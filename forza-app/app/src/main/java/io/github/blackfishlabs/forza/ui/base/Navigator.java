package io.github.blackfishlabs.forza.ui.base;

import android.content.Intent;
import android.net.Uri;

import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.ui.addorder.AddOrderActivity;
import io.github.blackfishlabs.forza.ui.citylist.CityListActivity;
import io.github.blackfishlabs.forza.ui.customerlist.CustomerListFragment;
import io.github.blackfishlabs.forza.ui.dashboard.DashboardFragment;
import io.github.blackfishlabs.forza.ui.importation.ImportResumeActivity;
import io.github.blackfishlabs.forza.ui.initialflow.InitialFlowActivity;
import io.github.blackfishlabs.forza.ui.main.AboutFragment;
import io.github.blackfishlabs.forza.ui.orderlist.OrderListPageFragment;
import io.github.blackfishlabs.forza.ui.ordersreport.OrdersReportFragment;
import io.github.blackfishlabs.forza.ui.productlist.ProductListFragment;
import io.github.blackfishlabs.forza.ui.settings.SettingsActivity;
import io.github.blackfishlabs.forza.ui.viewcustomer.ViewCustomerActivity;
import io.github.blackfishlabs.forza.ui.vieworder.ViewOrderActivity;

public class Navigator {

    public static final int REQUEST_INITIAL_FLOW = 0x1;
    public static final int REQUEST_SELECT_CITY = 0x2;
    public static final int REQUEST_SETTINGS = 0x3;

    private final BaseActivity mActivity;

    Navigator(final BaseActivity activity) {
        mActivity = activity;
    }

    public void toInitialFlow() {
        ActivityCompat.startActivityForResult(mActivity,
                new Intent(mActivity, InitialFlowActivity.class), REQUEST_INITIAL_FLOW, null);
    }

    public void toDashboard() {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_all_fragment_container,
                        DashboardFragment.newInstance(), DashboardFragment.TAG)
                .commit();

        FloatingActionButton fab = mActivity.findViewById(R.id.fab_all_main_action);
        fab.show();

        mActivity.setTitle(R.string.dashboard_fragment_title);
    }

    public void toOrdersReport() {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_all_fragment_container,
                        OrdersReportFragment.newInstance(), OrdersReportFragment.TAG)
                .commit();

        FloatingActionButton fab = mActivity.findViewById(R.id.fab_all_main_action);
        fab.hide();

        mActivity.setTitle(R.string.orders_report_fragment_title);
    }

    public void toOrderList() {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_all_fragment_container,
                        OrderListPageFragment.newInstance(), OrderListPageFragment.TAG)
                .commit();

        FloatingActionButton fab = mActivity.findViewById(R.id.fab_all_main_action);
        fab.show();

        mActivity.setTitle(R.string.order_list_fragment_title);
    }

    public void toCustomerList() {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_all_fragment_container,
                        CustomerListFragment.newInstance(), CustomerListFragment.TAG)
                .commit();

        FloatingActionButton fab = mActivity.findViewById(R.id.fab_all_main_action);
        fab.show();

        mActivity.setTitle(R.string.customer_list_fragment_title);
    }

    public void toProductList() {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_all_fragment_container,
                        ProductListFragment.newInstance(), ProductListFragment.TAG)
                .commit();

        FloatingActionButton fab = mActivity.findViewById(R.id.fab_all_main_action);
        fab.show();

        mActivity.setTitle(R.string.product_list_fragment_title);
    }

    public void toSettings() {
        ActivityCompat
                .startActivityForResult(mActivity,
                        new Intent(mActivity, SettingsActivity.class), REQUEST_SETTINGS, null);
    }

    public void toAbout() {
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_all_fragment_container,
                        AboutFragment.newInstance(), AboutFragment.TAG)
                .commit();

        FloatingActionButton fab = mActivity.findViewById(R.id.fab_all_main_action);
        fab.hide();

        mActivity.setTitle(R.string.main_drawer_item_about);
    }

    public void toImportResume() {
        ActivityCompat
                .startActivity(mActivity, new Intent(mActivity, ImportResumeActivity.class), null);
    }

//    public void toAddCustomer() {
//        ActivityCompat
//                .startActivity(mActivity, new Intent(mActivity, AddCustomerActivity.class), null);
//    }

    public void toViewCustomer() {
        ActivityCompat
                .startActivity(mActivity, new Intent(mActivity, ViewCustomerActivity.class), null);
    }

    public void toCityList() {
        ActivityCompat
                .startActivityForResult(mActivity, new Intent(mActivity, CityListActivity.class),
                        REQUEST_SELECT_CITY, null);
    }

    public void toAddOrder() {
        ActivityCompat
                .startActivity(mActivity, new Intent(mActivity, AddOrderActivity.class), null);
    }

    public void toViewOrder() {
        ActivityCompat
                .startActivity(mActivity, new Intent(mActivity, ViewOrderActivity.class), null);
    }

    public void toWebSite(String site) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(site));
        ActivityCompat.startActivity(mActivity, browserIntent, null);
    }

}
