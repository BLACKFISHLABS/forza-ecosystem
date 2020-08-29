package io.github.blackfishlabs.forza.ui.importation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.domain.pojo.Company;
import io.github.blackfishlabs.forza.ui.PresentationInjector;
import timber.log.Timber;

class ImportationAdapter extends RecyclerView.Adapter<ImportationViewHolder> {

    private final List<Company> mCompanies;

    private final int[] mProgressValues;

    ImportationAdapter(final List<Company> companies) {
        mCompanies = companies;
        mProgressValues = new int[companies.size()];
    }

    @Override
    public ImportationViewHolder onCreateViewHolder(
            final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_importation, parent, false);
        return new ImportationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImportationViewHolder holder, final int position) {
        holder.textViewCompanyName.setText(mCompanies.get(position).getName());

        final int progress = mProgressValues[position];
        Timber.d("setProgress of %d to %d", position, progress);

        holder.buttonRetry.setVisibility(progress == -1 ? View.VISIBLE : View.GONE);

        holder.buttonRetry.setOnClickListener(v -> {
            Company company = mCompanies.get(holder.getAdapterPosition());
            PresentationInjector.provideEventBus().post(RetryImportEvent.newEvent(company));
        });
    }

    @Override
    public int getItemCount() {
        return mCompanies.size();
    }

    void updateProgress(Company company, int progress) {
        int indexOf = mCompanies.indexOf(company);
        Timber.d("updateProgress of %d to %d", indexOf, progress);
        mProgressValues[indexOf] = progress;
        notifyItemChanged(indexOf);
    }
}
