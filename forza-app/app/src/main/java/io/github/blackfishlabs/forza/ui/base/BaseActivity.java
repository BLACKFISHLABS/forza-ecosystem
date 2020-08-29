package io.github.blackfishlabs.forza.ui.base;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NavUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.settings.SettingsRepository;
import io.github.blackfishlabs.forza.helper.ConnectivityHelper;
import io.github.blackfishlabs.forza.ui.PresentationInjector;

public abstract class BaseActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Navigator mNavigator;

    @Nullable
    @BindView(R.id.coordinator_layout_container)
    protected CoordinatorLayout mCoordinatorLayoutContainer;
    @Nullable
    @BindView(R.id.toolbar_all_actionbar)
    protected Toolbar mToolbar;
    @Nullable
    @BindView(R.id.progress_bar_all_loading)
    protected ProgressBar mProgressBar;
    @Nullable
    @BindView(R.id.linear_layout_all_error_state)
    protected LinearLayout mLinearLayoutErrorState;
    @Nullable
    @BindView(R.id.linear_layout_all_empty_state)
    protected LinearLayout mLinearLayoutEmptyState;

    @LayoutRes
    protected abstract int provideContentViewResource();

    @Override
    protected void onCreate(@Nullable final Bundle inState) {
        super.onCreate(inState);
        setContentView(provideContentViewResource());
        ButterKnife.bind(this);
        mNavigator = new Navigator(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getParentActivityIntent() == null) {
                onBackPressed();
            } else {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setAsHomeActivity() {
        setupToolbar(R.drawable.main_hamburguer_24dp);
    }

    protected void setAsInitialFlowActivity() {
        setupToolbar(R.drawable.all_clear_24dp);
    }

    protected void setAsSubActivity() {
        setupToolbar(R.drawable.all_arrow_back_24dp);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupToolbar(int drawableRes) {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeAsUpIndicator(drawableRes);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected Navigator navigate() {
        if (mNavigator == null) {
            mNavigator = new Navigator(this);
        }
        return mNavigator;
    }

    protected SettingsRepository settings() {
        return PresentationInjector.provideSettingsRepository();
    }

    protected EventBus eventBus() {
        return PresentationInjector.provideEventBus();
    }

    protected ConnectivityHelper connectivity() {
        return PresentationInjector.provideConnectivityHelper();
    }
}
