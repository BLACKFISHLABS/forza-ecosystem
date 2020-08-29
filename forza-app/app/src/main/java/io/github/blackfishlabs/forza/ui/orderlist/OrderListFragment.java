package io.github.blackfishlabs.forza.ui.orderlist;

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
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.fastadapter.IItemAdapter.Predicate;
import com.mikepenz.fastadapter.ISelectionListener;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter.listeners.OnLongClickListener;
import com.mikepenz.fastadapter_extensions.ActionModeHelper;
import com.mikepenz.materialize.util.UIUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.order.OrderRepository;
import io.github.blackfishlabs.forza.data.order.OrderStatusSpecificationFilter;
import io.github.blackfishlabs.forza.data.order.OrdersByUserSpecification;
import io.github.blackfishlabs.forza.data.sync.InstantSyncService;
import io.github.blackfishlabs.forza.data.sync.OrdersSyncedEvent;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.Order;
import io.github.blackfishlabs.forza.ui.addorder.orderreview.SavedOrderEvent;
import io.github.blackfishlabs.forza.ui.base.BaseFragment;
import io.github.blackfishlabs.forza.ui.main.LoggedInUserEvent;

import static androidx.core.content.ContextCompat.getColor;
import static java.util.Objects.requireNonNull;

public class OrderListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_SHOW_ONLY_PENDING_ORDERS = OrderListFragment.class.getName() + ".argShowOnlyPendingOrders";

    private OrderRepository orderRepository;

    private FastItemAdapter<OrderAdapterItem> fastItemAdapter;

    private ActionModeHelper actionModeHelper;

    private Predicate<OrderAdapterItem> filterPredicate =
            (item, constraint) -> !item.getOrder()
                    .getCustomer().getFantasyName().trim().toLowerCase()
                    .contains(constraint.toString().toLowerCase());

    private OnClickListener<OrderAdapterItem> preClickListener = (v, a, item, position) -> {
        if (actionModeHelper.isActive()) {
            Set<OrderAdapterItem> selectedItems = fastItemAdapter.getSelectedItems();
            int size = selectedItems.size();
            OrderAdapterItem[] selectedItemsArray =
                    selectedItems.toArray(new OrderAdapterItem[size]);

            Order firstOrder = selectedItemsArray[0].getOrder();
            Order selectedOrder = item.getOrder();

            if (!selectedOrder.isStatusEquals(firstOrder)) {
                Snackbar.make(requireNonNull(getView()), R.string.order_list_multi_selection_only_for_same_status, Snackbar.LENGTH_SHORT).show();
                return true;
            } else if (!selectedOrder.isStatusCreatedOrModified()) {
                if (selectedItems.contains(item) && selectedItems.size() == 1) {
                    actionModeHelper.reset();
                } else {
                    Snackbar.make(requireNonNull(getView()), R.string.order_list_single_selection_to_duplicate_order, Snackbar.LENGTH_SHORT).show();
                }
                return true;
            }
        }

        return actionModeHelper.onClick(item) != null;
    };

    private OnClickListener<OrderAdapterItem> clickListener = (v, a, i, position) -> {
        if (!actionModeHelper.isActive()) {
            final Order selectedOrder = fastItemAdapter.getAdapterItem(position).getOrder();
            if (selectedOrder != null) {
                eventBus().postSticky(SelectedOrderEvent.selectOrder(selectedOrder));
                if (selectedOrder.isStatusCreatedOrModified()) {
                    navigate().toAddOrder();
                } else {
                    navigate().toViewOrder();
                }
            }
        } else {
            actionModeHelper.getActionMode().invalidate();
        }

        return false;
    };

    private OnLongClickListener<OrderAdapterItem> preLongClickListener = (v, d, i, position) -> {
        ActionMode actionMode = actionModeHelper.onLongClick(getHostActivity(), position);

        if (actionMode != null) {
            getHostActivity()
                    .findViewById(R.id.action_mode_bar)
                    .setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(requireNonNull(getContext()),
                            R.attr.colorPrimary, R.color.material_drawer_primary));
        }

        return actionMode != null;
    };

    private ISelectionListener<OrderAdapterItem> selectionListener = (item, selected) -> {
        if (actionModeHelper.isActive()) {
            actionModeHelper.getActionMode().invalidate();
        }
    };

    protected OnGlobalLayoutListener recyclerViewLayoutListener = null;

    protected LoggedUser loggedUser;

    @BindView(R.id.swipe_container_all_pull_refresh)
    protected SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view_orders)
    protected RecyclerView recyclerViewOrders;

    public static OrderListFragment newInstance(boolean showOnlyPendingOrders) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SHOW_ONLY_PENDING_ORDERS, showOnlyPendingOrders);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int provideContentViewResource() {
        return R.layout.fragment_order_list;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(
            final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle inState) {
        View view = super.onCreateView(inflater, container, inState);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);

        orderRepository = LocalDataInjector.providerOrderRepository();

        fastItemAdapter = new FastItemAdapter<>();

        fastItemAdapter.setHasStableIds(true);
        fastItemAdapter.getItemFilter().withFilterPredicate(filterPredicate);

        fastItemAdapter
                .withSelectable(true)
                .withMultiSelect(true)
                .withSelectOnLongClick(true)
                .withSavedInstanceState(inState) // ! important
                .withOnPreClickListener(preClickListener)
                .withOnClickListener(clickListener)
                .withOnPreLongClickListener(preLongClickListener)
                .withSelectionListener(selectionListener);

        actionModeHelper = new ActionModeHelper(fastItemAdapter, R.menu.menu_cab_order_list, new ActionBarCallback());

        recyclerViewOrders.setHasFixedSize(true);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return getHostActivity().onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(
            @NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadOrders();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_all_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.order_list_search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fastItemAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                fastItemAdapter.filter(query);
                return true;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        menu.findItem(R.id.action_all_search).setVisible(recyclerViewOrders.getAdapter() != null);
    }

    @Override
    public void onRefresh() {
        loadOrders();
    }

    @OnClick(R.id.button_all_retry)
    void onButtonRetryClicked() {
        requireNonNull(mLinearLayoutErrorState).setVisibility(View.GONE);
        loadOrders();
    }

    @Subscribe(sticky = true)
    public void onLoggedInUserEvent(LoggedInUserEvent event) {
        if (loggedUser != null && !loggedUser.equals(event.getUser())) {
            loggedUser = event.getUser();
            loadOrders();
        }
    }

    @Subscribe(sticky = true)
    public void onSavedOrder(SavedOrderEvent event) {
        final Order order = event.getOrder();
        eventBus().removeStickyEvent(SavedOrderEvent.class);
    }

    @Subscribe
    public void onOrdersSynced(OrdersSyncedEvent event) {
        requireNonNull(getActivity()).runOnUiThread(() -> {
            loadOrders();
            if (event.isInstantly()) {
                loadOrders();
                Snackbar.make(requireNonNull(getView()), R.string.order_list_instant_sync_done, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void loadOrders() {
        OrdersByUserSpecification specification = new OrdersByUserSpecification(getSalesmanId(), getCompanyId()).orderByIssueDate();

        if (isShowOnlyPendingOrders()) {
            specification.byStatus(OrderStatusSpecificationFilter.CREATED_OR_MODIFIED);
        }

        startLoadingOrders();
        List<Order> orders = orderRepository.query(specification);
        showOrders(orders);
    }

    private void loadAllOrdersSynced() {
        OrdersByUserSpecification specification = new OrdersByUserSpecification(getSalesmanId(), getCompanyId()).orderByIssueDate();
        specification.byStatus(OrderStatusSpecificationFilter.SYNCED);
        startLoadingOrders();
        List<Order> orders = orderRepository.query(specification);
        showOrders(orders);
    }

    private int getSalesmanId() {
        return getLoggedUser().getSalesman().getSalesmanId();
    }

    private int getCompanyId() {
        return getLoggedUser().getDefaultCompany().getCompanyId();
    }

    private LoggedUser getLoggedUser() {
        if (loggedUser == null) {
            loggedUser = eventBus().getStickyEvent(LoggedInUserEvent.class).getUser();
        }
        return loggedUser;
    }

    private boolean isShowOnlyPendingOrders() {
        return getArguments() != null && getArguments().getBoolean(ARG_SHOW_ONLY_PENDING_ORDERS, false);
    }

    private void startLoadingOrders() {
        if (actionModeHelper.isActive()) {
            actionModeHelper.reset();
        }
        swipeRefreshLayout.setRefreshing(true);
        fastItemAdapter.clear();
        recyclerViewOrders.setAdapter(null);
        recyclerViewOrders.setVisibility(View.GONE);
        requireNonNull(mLinearLayoutEmptyState).setVisibility(View.VISIBLE);
        requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    private void showOrders(List<Order> orders) {
        if (!orders.isEmpty()) {
            recyclerViewOrders.setVisibility(View.VISIBLE);

            List<OrderAdapterItem> items = new ArrayList<>();
            for (Order order : orders) {
                items.add(OrderAdapterItem
                        .create(!isShowOnlyPendingOrders())
                        .withOrder(order)
                        .withIdentifier(order.getId()));
            }
            fastItemAdapter.add(items);
            recyclerViewOrders.setAdapter(fastItemAdapter);

            recyclerViewOrders
                    .getViewTreeObserver()
                    .addOnGlobalLayoutListener(
                            recyclerViewLayoutListener = this::onRecyclerViewFinishLoading);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            if (!eventBus().isRegistered(this)) {
                eventBus().register(this);
            }
        }
    }

    private void onRecyclerViewFinishLoading() {
        if (getView() != null) {
            recyclerViewOrders
                    .getViewTreeObserver()
                    .removeOnGlobalLayoutListener(recyclerViewLayoutListener);
            recyclerViewLayoutListener = null;
            requireNonNull(getActivity()).invalidateOptionsMenu();
            swipeRefreshLayout.setRefreshing(false);
            requireNonNull(mLinearLayoutEmptyState).setVisibility(View.GONE);
            if (!eventBus().isRegistered(this)) {
                eventBus().register(this);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState = fastItemAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
            requireNonNull(mLinearLayoutEmptyState).setVisibility(View.GONE);
        }
        eventBus().unregister(this);
        super.onDestroyView();
    }

    private class ActionBarCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(final ActionMode mode, final Menu menu) {
            requireNonNull(getActivity()).getWindow().setStatusBarColor(getColor(requireNonNull(getContext()), R.color.color_primary_dark));
            return true;
        }

        @Override
        public boolean onPrepareActionMode(final ActionMode mode, final Menu menu) {
            final int selectionSize = fastItemAdapter
                    .getSelectedItems().size();
            final MenuItem duplicateMenuItem = menu.findItem(R.id.action_duplicate_order);
            if (selectionSize == 1) {
                if (!duplicateMenuItem.isVisible()) {
                    duplicateMenuItem.setVisible(true);
                }

                OrderAdapterItem[] orderAdapterItems =
                        fastItemAdapter.getSelectedItems()
                                .toArray(new OrderAdapterItem[selectionSize]);
                Order firstOrder = orderAdapterItems[0].getOrder();
                if (!firstOrder.isStatusCreatedOrModified()) {
                    MenuItem syncItemsMenuItem = menu.findItem(R.id.action_sync);
                    if (syncItemsMenuItem.isVisible()) {
                        syncItemsMenuItem.setVisible(false);
                    }
                }

                return true;
            } else if (selectionSize > 1 && duplicateMenuItem.isVisible()) {
                duplicateMenuItem.setVisible(false);
                return true;
            }

            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_sync: {
                    Set<OrderAdapterItem> selectedItems = fastItemAdapter.getSelectedItems();
                    List<Order> orders = new ArrayList<>();
                    for (OrderAdapterItem orderAdapterItem : selectedItems) {
                        if (orderAdapterItem.getOrder().isStatusCreatedOrModified()) {
                            orders.add(orderAdapterItem.getOrder());
                        }
                    }
                    eventBus().postSticky(SyncOrdersEvent.just(orders));
                    InstantSyncService.execute(requireNonNull(getContext()));

                    // refresh screen
                    loadAllOrdersSynced();

                    // clear items
                    fastItemAdapter.getSelectedItems().clear();
                    fastItemAdapter.clear();
                    break;
                }
                case R.id.action_duplicate_order: {
                    Set<OrderAdapterItem> selectedItems = fastItemAdapter.getSelectedItems();
                    if (selectedItems != null && selectedItems.size() == 1) {
                        OrderAdapterItem orderAdapterItem = selectedItems.iterator().next();
                        eventBus().postSticky(SelectedOrderEvent.duplicateOrder(orderAdapterItem.getOrder()));
                        navigate().toAddOrder();
                    }

                    // clear items
                    fastItemAdapter.getSelectedItems().clear();
                    fastItemAdapter.clear();
                    break;
                }
            }

            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }
}
