package io.github.blackfishlabs.forza.ui.addorder.orderitems;

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

import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.pricetable.PriceTableRepository;
import io.github.blackfishlabs.forza.domain.entity.PriceTableEntity;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.Order;
import io.github.blackfishlabs.forza.domain.pojo.OrderItem;
import io.github.blackfishlabs.forza.domain.pojo.PriceTable;
import io.github.blackfishlabs.forza.domain.pojo.PriceTableItem;
import io.github.blackfishlabs.forza.ui.base.BaseFragment;
import io.github.blackfishlabs.forza.ui.customerlist.SelectedCustomerEvent;
import io.github.blackfishlabs.forza.ui.main.LoggedInUserEvent;
import io.github.blackfishlabs.forza.ui.orderlist.SelectedOrderEvent;

import static android.R.color.white;
import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.Integer.valueOf;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

public class SelectOrderItemsStepFragment extends BaseFragment implements Step, SelectOrderItemsCallbacks {

    private PriceTableRepository priceTableRepository;
    private SelectOrderItemsAdapter selectOrderItemsAdapter;
    private OnGlobalLayoutListener recyclerViewLayoutListener = null;
    private LoggedUser loggedUser;
    private Integer customerDefaultPriceTable;
    private List<OrderItem> orderItems;
    private Set<OrderItem> selectedOrderItems;
    private Order selectedOrder;
    private SearchView searchView;

    @BindView(R.id.swipe_container_all_pull_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view_order_items)
    RecyclerView recyclerViewOrderItems;

    public static SelectOrderItemsStepFragment newInstance() {
        return new SelectOrderItemsStepFragment();
    }

    @Override
    protected int provideContentViewResource() {
        return R.layout.fragment_select_order_items;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        SelectedOrderEvent event = eventBus().getStickyEvent(SelectedOrderEvent.class);
        if (event != null && event.getOrder() != null) {
            selectedOrder = event.getOrder();
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle inState) {
        final View view = super.onCreateView(inflater, container, inState);

        recyclerViewOrderItems.setHasFixedSize(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(white);

        priceTableRepository = LocalDataInjector.providePriceTableRepository();

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProducts();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_all_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.select_order_items_search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (selectOrderItemsAdapter != null) {
                    selectOrderItemsAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        menu.findItem(R.id.action_all_search).setVisible(selectOrderItemsAdapter != null && !selectOrderItemsAdapter.isEmptyList());
    }

    @OnClick(R.id.button_all_retry)
    void onButtonRetryClicked() {
        requireNonNull(mLinearLayoutErrorState).setVisibility(GONE);
        loadProducts();
    }

    @Subscribe(sticky = true)
    public void onLoggedInUser(LoggedInUserEvent event) {
        if (loggedUser != null && !loggedUser.equals(event.getUser())) {
            loggedUser = event.getUser();
            loadProducts();
        }
    }

    @Subscribe(priority = 1)
    public void onSelectedCustomer(SelectedCustomerEvent event) {
        final String defaultPriceTable = event.getCustomer().getDefaultPriceTable();
        if (!isEmpty(defaultPriceTable) && !isEmpty(defaultPriceTable.trim())) {
            final Integer defaultPriceTableId = valueOf(defaultPriceTable.trim());
            if (!defaultPriceTableId.equals(customerDefaultPriceTable)) {
                customerDefaultPriceTable = defaultPriceTableId;
                loadProducts();
            }
        } else {
            if (customerDefaultPriceTable != null) {
                customerDefaultPriceTable = null;
                loadProducts();
            }
        }
    }

    @Override
    public void onAddOrderItemRequested(final OrderItem orderItem, final int position) {
        final float quantity = orderItem.getQuantity() + 1;
        if (checkQuantity(quantity)) {
            orderItem.addQuantity(1);
            selectOrderItemsAdapter.notifyItemChanged(position);
            selectedOrderItems.add(orderItem);
        }
    }

    @Override
    public void onRemoveOrderItemRequested(
            final OrderItem orderItem, final int position) {
        final Float quantity = orderItem.getQuantity();
        if (quantity != null && quantity >= 1) {
            orderItem.removeOneFromQuantity();
            selectOrderItemsAdapter.notifyItemChanged(position);
            if (orderItem.getQuantity() == 0)
                selectedOrderItems.remove(orderItem);
        }
    }

    @Override
    public void onChangeOrderItemQuantityRequested(final OrderItem orderItem, final float quantity, final int position) {
        if (checkQuantity(quantity)) {
            orderItem.withQuantity(quantity);
            selectOrderItemsAdapter.notifyItemChanged(position);
            if (orderItem.getQuantity() == 0)
                selectedOrderItems.remove(orderItem);
            else
                selectedOrderItems.add(orderItem);
        }
    }

    @Override
    public void onChangeOrderItemPriceRequested(OrderItem orderItem, double price, int position) {
        if (checkPrice(price)) {
            orderItem.withSalesPrice(price);
            selectOrderItemsAdapter.notifyItemChanged(position);
            if (orderItem.getSalesPrice() == 0)
                selectedOrderItems.remove(orderItem);
            else
                selectedOrderItems.add(orderItem);
        }
    }

    @Override
    public VerificationError verifyStep() {
        if (!isEmpty(searchView.getQuery())) {
            searchView.setQuery("", false);
            searchView.clearFocus();
            selectOrderItemsAdapter.getFilter().filter("");
        }

        List<OrderItem> addedOrderItems = getSelectedOrderItems();
        if (!addedOrderItems.isEmpty()) {
            eventBus().post(AddedOrderItemsEvent.newEvent(addedOrderItems));
            return null;
        } else {
            return new VerificationError(getString(R.string.select_order_items_no_order_items));
        }
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull final VerificationError error) {
        Snackbar.make(requireNonNull(getView()), error.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
    }

    private void loadProducts() {
        startLoadingProducts();
        List<PriceTable> priceTable = priceTableRepository.findAll(PriceTableEntity.Fields.PRICE_TABLE_ID, true);

        if (!priceTable.isEmpty()) {
            PriceTable next = priceTable.iterator().next();
            eventBus().post(SelectedPriceTableEvent.newEvent(next));
            List<PriceTableItem> items = next.getItems();

            for (PriceTableItem item : items) {
                OrderItem orderItem = createOrderItem(item);
                orderItems.add(orderItem);
            }
        }

        showOrderItems();
    }

    private OrderItem createOrderItem(final PriceTableItem priceTableItem) {
        OrderItem orderItem;
        if (selectedOrder != null) {
            for (final OrderItem item : selectedOrder.getItems()) {
                if (item.getItem().equals(priceTableItem)) {
                    orderItem = new OrderItem()
                            .withTempId(UUID.randomUUID().toString())
                            .withItem(priceTableItem)
                            .withSalesPrice(item.getSalesPrice())
                            .withQuantity(item.getQuantity());

                    if (selectedOrderItems == null)
                        selectedOrderItems = new LinkedHashSet<>();
                    selectedOrderItems.add(orderItem);
                    return orderItem;
                }
            }
        }

        orderItem = new OrderItem()
                .withTempId(UUID.randomUUID().toString())
                .withItem(priceTableItem)
                .withSalesPrice(priceTableItem.getSalesPrice())
                .withQuantity(0.0f);
        return orderItem;
    }

    private void startLoadingProducts() {
        if (orderItems == null) {
            orderItems = new ArrayList<>();
            selectedOrderItems = new LinkedHashSet<>();
        } else {
            orderItems.clear();
            selectedOrderItems.clear();
        }
        swipeRefreshLayout.setRefreshing(true);
        selectOrderItemsAdapter = null;
        recyclerViewOrderItems.setAdapter(null);
        recyclerViewOrderItems.setVisibility(GONE);
        requireNonNull(mLinearLayoutEmptyState).setVisibility(VISIBLE);
        requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    private void showOrderItems() {
        if (!orderItems.isEmpty()) {
            recyclerViewOrderItems.setVisibility(VISIBLE);
            recyclerViewOrderItems.setAdapter(
                    selectOrderItemsAdapter = new SelectOrderItemsAdapter(
                            orderItems, this, getContext()));
            recyclerViewOrderItems
                    .getViewTreeObserver()
                    .addOnGlobalLayoutListener(
                            recyclerViewLayoutListener = this::onRecyclerViewFinishLoading);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void onRecyclerViewFinishLoading() {
        if (getView() != null) {
            recyclerViewOrderItems
                    .getViewTreeObserver()
                    .removeOnGlobalLayoutListener(recyclerViewLayoutListener);
            recyclerViewLayoutListener = null;

            requireNonNull(getActivity()).invalidateOptionsMenu();
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);
            requireNonNull(mLinearLayoutEmptyState).setVisibility(GONE);
            if (!eventBus().isRegistered(this)) {
                eventBus().register(this);
            }
        }
    }

    private boolean checkQuantity(final float quantity) {
        if (quantity < 0) {
            Snackbar.make(requireNonNull(getView()), R.string.select_order_items_invalid_quantity, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkPrice(final double price) {
        if (price <= 0) {
            Snackbar.make(requireNonNull(getView()), R.string.select_order_items_invalid_price, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private List<OrderItem> getSelectedOrderItems() {
        if (selectedOrderItems == null || selectedOrderItems.isEmpty()) {
            return emptyList();
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (final OrderItem item : selectedOrderItems) {
            if (item.getQuantity() > 0) {
                orderItems.add(item);
            }
        }
        return orderItems;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        eventBus().unregister(this);
    }
}
