package io.github.blackfishlabs.forza.ui.initialflow;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.ui.base.BaseActivity;
import io.github.blackfishlabs.forza.ui.importation.CompletedImportationEvent;
import io.github.blackfishlabs.forza.ui.importation.ImportationFragment;
import io.github.blackfishlabs.forza.ui.login.CompletedLoginEvent;
import io.github.blackfishlabs.forza.ui.login.LoginFragment;
import io.github.blackfishlabs.forza.ui.settings.CompletedSettingsEvent;
import io.github.blackfishlabs.forza.ui.settings.SettingsFragment;

public class InitialFlowActivity extends BaseActivity {

    @BindView(R.id.coordinator_layout_container)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected int provideContentViewResource() {
        return R.layout.activity_initial_flow;
    }

    @Override
    protected void onCreate(@Nullable final Bundle inState) {
        super.onCreate(inState);
        setAsInitialFlowActivity();

        if (!settings().isAllSettingsPresent()) {
            navigateToSettings();
        } else if (!settings().isUserLoggedIn()) {
            navigateToLogin();
        } else {
            navigateToImportation();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus().register(this);
    }

    @Override
    protected void onStop() {
        eventBus().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onCompletedSettings(CompletedSettingsEvent event) {
        navigateToLogin();
    }

    @Subscribe
    public void onCompletedLogin(CompletedLoginEvent event) {
        navigateToImportation();
    }

    @Subscribe
    public void onCompletedImportation(CompletedImportationEvent event) {
        finishFlow();
    }

    private void navigateToSettings() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout_all_fragment_container,
                        SettingsFragment.newInstanceForInitialFlow(), SettingsFragment.TAG)
                .commit();
    }

    private void navigateToLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_all_fragment_container,
                        LoginFragment.newInstance(), LoginFragment.TAG)
                .commitNow();
        setTitle(R.string.initial_flow_login_title);
    }

    private void navigateToImportation() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_all_fragment_container,
                        ImportationFragment.newInstance(), ImportationFragment.TAG)
                .commitNow();
        setTitle(R.string.initial_flow_importation_title);
    }

    private void finishFlow() {
        setResult(RESULT_OK);
        finish();
    }
}
