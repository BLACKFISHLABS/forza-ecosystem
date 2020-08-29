package io.github.blackfishlabs.forza.ui.customerlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.company.customer.CustomersByCompanySpecification;
import io.github.blackfishlabs.forza.data.customer.CustomerRepository;
import io.github.blackfishlabs.forza.data.sync.CustomersSyncedEvent;
import io.github.blackfishlabs.forza.domain.pojo.Customer;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.ui.addcustomer.SavedCustomerEvent;
import io.github.blackfishlabs.forza.ui.base.BaseFragment;
import io.github.blackfishlabs.forza.ui.main.LoggedInUserEvent;
import io.github.blackfishlabs.forza.ui.widget.recyclerview.OnItemClickListener;
import io.github.blackfishlabs.forza.ui.widget.recyclerview.OnItemTouchListener;

import static java.util.Objects.requireNonNull;

public class CustomerListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    public static final String TAG = CustomerListFragment.class.getName();

    protected SearchView mSearchView;

    protected CustomerRepository mCustomerRepository;

    protected CustomerListAdapter mCustomerListAdapter;

    protected OnGlobalLayoutListener mRecyclerViewLayoutListener = null;

    protected OnItemTouchListener mRecyclerViewItemTouchListener = null;

    protected LoggedUser mLoggedUser;

    @BindView(R.id.swipe_container_all_pull_refresh)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view_customers)
    protected RecyclerView mRecyclerViewCustomers;

    public static CustomerListFragment newInstance() {
        return new CustomerListFragment();
    }

    @Override
    protected int provideContentViewResource() {
        return R.layout.fragment_customer_list;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle inState) {
        View view = super.onCreateView(inflater, container, inState);

        mRecyclerViewCustomers.setHasFixedSize(true);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);

        mCustomerRepository = LocalDataInjector.provideCustomerRepository();

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadCustomers();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_all_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint(getString(R.string.customer_list_search_hint));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mCustomerListAdapter != null) {
                    mCustomerListAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        menu.findItem(R.id.action_all_search)
                .setVisible(mCustomerListAdapter != null && !mCustomerListAdapter.isEmptyList());
    }

    @Override
    public void onRefresh() {
        loadCustomers();
    }

    @Override
    public void onSingleTapUp(final View view, final int position) {
        Customer selectedCustomer = mCustomerListAdapter.getCustomer(position);
        if (selectedCustomer != null) {
            onSelectedCustomerFromList(selectedCustomer);
        }
    }

    @Override
    public void onLongPress(final View view, final int position) {
    }

    @OnClick(R.id.button_all_retry)
    void onButtonRetryClicked() {
        mLinearLayoutErrorState.setVisibility(View.GONE);
        loadCustomers();
    }

    @Subscribe(sticky = true)
    public void onLoggedInUserEvent(LoggedInUserEvent event) {
        if (mLoggedUser != null && !mLoggedUser.equals(event.getUser())) {
            mLoggedUser = event.getUser();
            loadCustomers();
        }
    }

    @Subscribe(sticky = true)
    public void onSavedCustomer(SavedCustomerEvent event) {
        final Customer customer = event.getCustomer();
        eventBus().removeStickyEvent(SavedCustomerEvent.class);
        if (mCustomerListAdapter != null) {
            final int position = mCustomerListAdapter.updateCustomer(customer);
            if (position != RecyclerView.NO_POSITION) {
                mRecyclerViewCustomers.scrollToPosition(position);
            }
        } else {
            showCustomers(new ArrayList<>(Collections.singleton(customer)));
        }
    }

    @Subscribe
    public void onCustomersSynced(CustomersSyncedEvent event) {
        requireNonNull(getActivity()).runOnUiThread(this::loadCustomers);
    }

    private void loadCustomers() {
        startLoadingCustomers();
        List<Customer> customers = mCustomerRepository.query(new CustomersByCompanySpecification(loadSelectedCompanyId()));
        showCustomers(customers);
    }

    private int loadSelectedCompanyId() {
        if (mLoggedUser == null) {
            mLoggedUser = eventBus().getStickyEvent(LoggedInUserEvent.class).getUser();
        }
        return mLoggedUser.getDefaultCompany().getCompanyId();
    }

    private void startLoadingCustomers() {
        mSwipeRefreshLayout.setRefreshing(true);
        mCustomerListAdapter = null;
        mRecyclerViewCustomers.setAdapter(null);
        mRecyclerViewCustomers.setVisibility(View.GONE);
        requireNonNull(mLinearLayoutEmptyState).setVisibility(View.VISIBLE);
        requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    private void showCustomers(List<Customer> customers) {
        if (!customers.isEmpty()) {
            mRecyclerViewCustomers.setVisibility(View.VISIBLE);
            mRecyclerViewCustomers.setAdapter(mCustomerListAdapter = new CustomerListAdapter(customers));
            mRecyclerViewCustomers.getViewTreeObserver()
                    .addOnGlobalLayoutListener(
                            mRecyclerViewLayoutListener = this::onRecyclerViewFinishLoading);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    protected void onRecyclerViewFinishLoading() {
        if (getView() != null) {
            mRecyclerViewCustomers
                    .getViewTreeObserver()
                    .removeOnGlobalLayoutListener(mRecyclerViewLayoutListener);
            mRecyclerViewLayoutListener = null;

            if (mRecyclerViewItemTouchListener != null) {
                mRecyclerViewCustomers.removeOnItemTouchListener(mRecyclerViewItemTouchListener);
                mRecyclerViewItemTouchListener = null;
            }

            mRecyclerViewCustomers.addOnItemTouchListener(
                    mRecyclerViewItemTouchListener
                            = new OnItemTouchListener(requireNonNull(getContext()), mRecyclerViewCustomers, this));

            requireNonNull(getActivity()).invalidateOptionsMenu();
            mSwipeRefreshLayout.setRefreshing(false);
            requireNonNull(mLinearLayoutEmptyState).setVisibility(View.GONE);
            if (!eventBus().isRegistered(this)) {
                eventBus().register(this);
            }
        }
    }

    protected void onSelectedCustomerFromList(@NonNull Customer selectedCustomer) {
        eventBus().postSticky(SelectedCustomerEvent.selectCustomer(selectedCustomer));
        navigate().toViewCustomer();
    }

    @Override
    public void onDestroyView() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            requireNonNull(mLinearLayoutEmptyState).setVisibility(View.GONE);
        }
        eventBus().unregister(this);
        super.onDestroyView();
    }
}
