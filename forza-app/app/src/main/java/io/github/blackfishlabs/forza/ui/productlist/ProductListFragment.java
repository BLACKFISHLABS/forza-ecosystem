package io.github.blackfishlabs.forza.ui.productlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.common.collect.Lists;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.pricetable.PriceTableRepository;
import io.github.blackfishlabs.forza.data.sync.ProductsUpdatedEvent;
import io.github.blackfishlabs.forza.domain.entity.PriceTableEntity;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.PriceTable;
import io.github.blackfishlabs.forza.domain.pojo.PriceTableItem;
import io.github.blackfishlabs.forza.ui.base.BaseFragment;
import io.github.blackfishlabs.forza.ui.main.LoggedInUserEvent;

import static java.util.Objects.requireNonNull;

public class ProductListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = ProductListFragment.class.getName();

    private SearchView mSearchView;

    private PriceTableRepository mPriceTableRepository;

    private ProductListAdapter mProductListAdapter;

    private OnGlobalLayoutListener mRecyclerViewLayoutListener = null;

    private LoggedUser mLoggedUser;

    @BindView(R.id.swipe_container_all_pull_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view_products)
    RecyclerView mRecyclerViewProducts;

    public static ProductListFragment newInstance() {
        return new ProductListFragment();
    }

    @Override
    protected int provideContentViewResource() {
        return R.layout.fragment_product_list;
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

        mRecyclerViewProducts.setHasFixedSize(true);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);

        mPriceTableRepository = LocalDataInjector.providePriceTableRepository();

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProducts();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_all_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint(getString(R.string.product_list_search_hint));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mProductListAdapter != null) {
                    mProductListAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        menu.findItem(R.id.action_all_search)
                .setVisible(mProductListAdapter != null && !mProductListAdapter.isEmptyList());
    }

    @Override
    public void onRefresh() {
        loadProducts();
    }

    @OnClick(R.id.button_all_retry)
    void onButtonRetryClicked() {
        requireNonNull(mLinearLayoutErrorState).setVisibility(View.GONE);
        loadProducts();
    }

    @Subscribe(sticky = true)
    public void onLoggedInUserEvent(LoggedInUserEvent event) {
        if (mLoggedUser != null && !mLoggedUser.equals(event.getUser())) {
            mLoggedUser = event.getUser();
            loadProducts();
        }
    }

    @Subscribe
    public void onProductsUpdated(ProductsUpdatedEvent event) {
        requireNonNull(getActivity()).runOnUiThread(this::loadProducts);
    }

    private void loadProducts() {
        startLoadingProducts();
        List<PriceTable> priceTable = mPriceTableRepository.findAll(PriceTableEntity.Fields.PRICE_TABLE_ID, true);

        if (priceTable.isEmpty())
            showProducts(Lists.newArrayList());
        else {
            PriceTable next = priceTable.iterator().next();
            List<PriceTableItem> items = next.getItems();
            showProducts(items);
        }
    }

//    private int loadDefaultPriceTableId() {
//        if (mLoggedUser == null) {
//            mLoggedUser = eventBus().getStickyEvent(LoggedInUserEvent.class).getUser();
//        }
//        return mLoggedUser.getDefaultCompany().getPriceTableId();
//    }

    private void startLoadingProducts() {
        mSwipeRefreshLayout.setRefreshing(true);
        mProductListAdapter = null;
        mRecyclerViewProducts.setAdapter(null);
        mRecyclerViewProducts.setVisibility(View.GONE);
        requireNonNull(mLinearLayoutEmptyState).setVisibility(View.VISIBLE);
        requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    private void showProducts(List<PriceTableItem> priceTableItems) {
        if (!priceTableItems.isEmpty()) {
            mRecyclerViewProducts.setVisibility(View.VISIBLE);
            mRecyclerViewProducts.setAdapter(
                    mProductListAdapter = new ProductListAdapter(priceTableItems));
            mRecyclerViewProducts
                    .getViewTreeObserver()
                    .addOnGlobalLayoutListener(
                            mRecyclerViewLayoutListener = this::onRecyclerViewFinishLoading);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void onRecyclerViewFinishLoading() {
        if (getView() != null) {
            mRecyclerViewProducts
                    .getViewTreeObserver()
                    .removeOnGlobalLayoutListener(mRecyclerViewLayoutListener);
            mRecyclerViewLayoutListener = null;

            requireNonNull(getActivity()).invalidateOptionsMenu();
            mSwipeRefreshLayout.setRefreshing(false);
            requireNonNull(mLinearLayoutEmptyState).setVisibility(View.GONE);
            if (!eventBus().isRegistered(this)) {
                eventBus().register(this);
            }
        }
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
