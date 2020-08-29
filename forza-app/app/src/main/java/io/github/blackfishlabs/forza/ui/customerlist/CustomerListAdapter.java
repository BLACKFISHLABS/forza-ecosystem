package io.github.blackfishlabs.forza.ui.customerlist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.domain.pojo.Customer;
import io.github.blackfishlabs.forza.helper.FormattingUtils;
import io.github.blackfishlabs.forza.helper.Mask;
import io.github.blackfishlabs.forza.ui.base.BaseFilter;

import static android.text.TextUtils.isEmpty;
import static android.view.LayoutInflater.from;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListViewHolder> implements Filterable {

    private final List<Customer> mCustomers;
    private List<Customer> mCustomersOriginalCopy;
    private CustomerListFilter mFilter;

    CustomerListAdapter(final List<Customer> customers) {
        mCustomers = customers;
    }

    @Override
    public CustomerListViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = from(parent.getContext()).inflate(R.layout.list_item_customer, parent, false);
        return new CustomerListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomerListViewHolder holder, final int position) {
        final Customer customer = mCustomers.get(position);
        final Context context = holder.itemView.getContext();

        holder.textViewSocialName.setText(customer.getName());
        holder.textViewCpfOrCnpj.setText(FormattingUtils.formatCpforCnpj(Mask.unmask(customer.getCpfOrCnpj())));

        if (!isEmpty(customer.getMainPhone()) && !isEmpty(customer.getSecondaryPhone())) {
            holder.textViewPhone.setText(
                    context.getString(R.string.customer_list_template_text_phones,
                            FormattingUtils.formatPhoneNumber(customer.getMainPhone()),
                            FormattingUtils.formatPhoneNumber(customer.getSecondaryPhone())));
        } else if (!isEmpty(customer.getMainPhone())) {
            holder.textViewPhone.setText(FormattingUtils.formatPhoneNumber(customer.getMainPhone()));
        } else if (!isEmpty(customer.getSecondaryPhone())) {
            holder.textViewPhone.setText(FormattingUtils.formatPhoneNumber(customer.getSecondaryPhone()));
        } else {
            holder.textViewPhone.setText(context.getString(R.string.customer_list_text_no_phone));
        }

        if (!isEmpty(customer.getEmail())) {
            holder.textViewEmail.setText(customer.getEmail());
        } else {
            holder.textViewEmail.setText(context.getString(R.string.customer_list_text_no_email));
        }

        if (!isEmpty(customer.getFantasyName())) {
            holder.textViewFantasyName.setText(customer.getFantasyName());
        } else {
            holder.textViewFantasyName
                    .setText(context.getString(R.string.customer_list_text_no_fantasy_name));
        }

        if (!isEmpty(customer.getCode())) {
            holder.textViewCustomerCode
                    .setText(context.getString(R.string.customer_list_template_customer_code,
                            customer.getCode()));
        } else {
            holder.textViewCustomerCode
                    .setText(context.getString(R.string.customer_list_text_no_customer_code));
        }
    }

    @Override
    public int getItemCount() {
        return mCustomers.size();
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new CustomerListFilter();
        }
        return mFilter;
    }

    public boolean isEmptyList() {
        return getItemCount() == 0;
    }

    public Customer getCustomer(@IntRange(from = 0) int position) {
        if (position < 0 || position >= mCustomers.size()) {
            return null;
        }
        return mCustomers.get(position);
    }

    public int updateCustomer(final Customer customer) {
        if (customer != null) {
            final int currentPosition = mCustomers.indexOf(customer);
            if (currentPosition == RecyclerView.NO_POSITION) {
                int lastPosition = mCustomers.size();
                mCustomers.add(lastPosition, customer);
                notifyItemInserted(lastPosition);
                return lastPosition;
            } else {
                mCustomers.set(currentPosition, customer);
                notifyItemChanged(currentPosition);
                return currentPosition;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    public int getItemPosition(@NonNull Customer customer) {
        return mCustomers.indexOf(customer);
    }

    private class CustomerListFilter extends BaseFilter<Customer> {

        CustomerListFilter() {
            super(CustomerListAdapter.this, mCustomers, mCustomersOriginalCopy);
        }

        @Override
        protected String[] filterValues(final Customer customer) {
            return new String[]{customer.getFantasyName(), customer.getContact(),
                    customer.getCpfOrCnpj(), customer.getCode()};
        }
    }
}
