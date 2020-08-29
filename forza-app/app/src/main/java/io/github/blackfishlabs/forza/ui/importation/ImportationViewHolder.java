package io.github.blackfishlabs.forza.ui.importation;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.blackfishlabs.forza.R;

class ImportationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view_importation_company_name)
    TextView textViewCompanyName;
    @BindView(R.id.button_all_retry)
    Button buttonRetry;

    ImportationViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
