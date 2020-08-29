package io.github.blackfishlabs.forza.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.data.settings.SettingsRepository;
import io.github.blackfishlabs.forza.helper.ConnectivityHelper;

public abstract class BaseFragment extends Fragment {

    private BaseActivity hostActivity;
    private Unbinder mUnbinder;

    @Nullable
    @BindView(R.id.linear_layout_all_error_state)
    protected LinearLayout mLinearLayoutErrorState;
    @Nullable
    @BindView(R.id.linear_layout_all_empty_state)
    protected LinearLayout mLinearLayoutEmptyState;

    public BaseFragment() {
    }

    @LayoutRes
    protected abstract int provideContentViewResource();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            hostActivity = (BaseActivity) context;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Descendants of " + getClass().getName() +
                    " must be hosted by " + BaseActivity.class.getName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle inState) {
        final View fragmentView = inflater.inflate(provideContentViewResource(), container, false);
        mUnbinder = ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    protected Navigator navigate() {
        return hostActivity.navigate();
    }

    protected SettingsRepository settings() {
        return hostActivity.settings();
    }

    protected EventBus eventBus() {
        return hostActivity.eventBus();
    }

    protected ConnectivityHelper connectivity() {
        return hostActivity.connectivity();
    }

    protected void setTitle(String title) {
        hostActivity.setTitle(title);
    }

    protected BaseActivity getHostActivity() {
        return hostActivity;
    }

}
