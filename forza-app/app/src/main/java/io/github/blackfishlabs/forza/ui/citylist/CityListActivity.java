package io.github.blackfishlabs.forza.ui.citylist;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arlib.floatingsearchview.FloatingSearchView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.city.CitiesByStateSpecification;
import io.github.blackfishlabs.forza.data.city.CityRepository;
import io.github.blackfishlabs.forza.domain.pojo.City;
import io.github.blackfishlabs.forza.ui.addcustomer.SelectedStateEvent;
import io.github.blackfishlabs.forza.ui.base.BaseActivity;
import io.github.blackfishlabs.forza.ui.widget.recyclerview.OnItemClickListener;
import io.github.blackfishlabs.forza.ui.widget.recyclerview.OnItemTouchListener;

import static java.util.Objects.requireNonNull;

public class CityListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    private CityRepository mCityRepository;

    private CityListAdapter mCityListAdapter;

    private SelectedStateEvent mSelectedStateEvent;

    private OnGlobalLayoutListener mRecyclerViewLayoutListener = null;

    private OnItemTouchListener mRecyclerViewItemTouchListener = null;

    @BindView(R.id.swipe_container_all_pull_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view_cities)
    RecyclerView mRecyclerViewCities;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;

    @Override
    protected int provideContentViewResource() {
        return R.layout.activity_city_list;
    }

    @Override
    protected void onCreate(@Nullable final Bundle inState) {
        super.onCreate(inState);
        setUpSearchView();
        setUpSwipeRefreshLayout();

        mCityRepository = LocalDataInjector.provideCityRepository();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadCities();
    }

    @OnClick(R.id.button_all_retry)
    void onButtonRetryClicked() {
        requireNonNull(mLinearLayoutErrorState).setVisibility(View.GONE);
        loadCities();
    }

    @Override
    public void onRefresh() {
        loadCities();
    }

    @Override
    public void onSingleTapUp(final View view, final int position) {
        selectCity(position);
    }

    @Override
    public void onLongPress(final View view, final int position) {
    }

    private void setUpSearchView() {
        mSearchView.setOnHomeActionClickListener(this::finish);
        mSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            if (mCityListAdapter != null) {
                mCityListAdapter.getFilter().filter(newQuery);
            }
        });
    }

    private void setUpSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
    }

    private void loadCities() {
        mSelectedStateEvent = eventBus().getStickyEvent(SelectedStateEvent.class);
        if (mSelectedStateEvent != null) {
            startLoadData();
            List<City> cities = queryCities(mSelectedStateEvent);
            showCities(cities);
        }
    }

    private List<City> queryCities(SelectedStateEvent event) {
        return mCityRepository.query(new CitiesByStateSpecification(event.getState().getStateId()));
    }

    private void startLoadData() {
        mRecyclerViewCities.setVisibility(View.GONE);
        requireNonNull(mLinearLayoutEmptyState).setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        mSearchView.showProgress();
    }

    private void showCities(List<City> cities) {
        if (!TextUtils.isEmpty(mSearchView.getQuery())) {
            mSearchView.clearQuery();
            mSearchView.clearSearchFocus();
        }

        if (!cities.isEmpty()) {
            mRecyclerViewCities.setVisibility(View.VISIBLE);
            mRecyclerViewCities.setHasFixedSize(true);
            mRecyclerViewCities.setAdapter(mCityListAdapter = new CityListAdapter(cities));
            mRecyclerViewCities
                    .getViewTreeObserver()
                    .addOnGlobalLayoutListener(
                            mRecyclerViewLayoutListener = this::onRecyclerViewFinishLoading);
        } else {
            requireNonNull(mLinearLayoutEmptyState).setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void onRecyclerViewFinishLoading() {
        mRecyclerViewCities
                .getViewTreeObserver()
                .removeOnGlobalLayoutListener(mRecyclerViewLayoutListener);
        mRecyclerViewLayoutListener = null;

        if (mRecyclerViewItemTouchListener != null) {
            mRecyclerViewCities.removeOnItemTouchListener(mRecyclerViewItemTouchListener);
            mRecyclerViewItemTouchListener = null;
        }

        mRecyclerViewCities.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerViewCities.addOnItemTouchListener(
                mRecyclerViewItemTouchListener
                        = new OnItemTouchListener(this, mRecyclerViewCities, this));

        mSearchView.hideProgress();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void selectCity(int position) {
        final City citySelected = mCityListAdapter.getCity(position);
        if (citySelected != null) {
            eventBus().postSticky(SelectedCityEvent.newEvent(citySelected));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSelectedStateEvent != null) {
            eventBus().removeStickyEvent(mSelectedStateEvent);
        }
    }
}
