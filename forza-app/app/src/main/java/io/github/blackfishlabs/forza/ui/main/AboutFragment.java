package io.github.blackfishlabs.forza.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.ui.base.BaseFragment;

public class AboutFragment extends BaseFragment {

    public static final String TAG = AboutFragment.class.getName();

    @BindView(R.id.bfLabs)
    TextView mGoToWebSite;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    protected int provideContentViewResource() {
        return R.layout.fragment_about;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle inState) {
        View view = super.onCreateView(inflater, container, inState);

        mGoToWebSite.setOnClickListener(v -> navigate().toWebSite("http://blackfishlabs.github.io"));

        return view;
    }
}
