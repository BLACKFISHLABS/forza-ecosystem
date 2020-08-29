package io.github.blackfishlabs.forza.ui.customerlist;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.blackfishlabs.forza.R;

class CustomerListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view_social_name)
    TextView textViewSocialName;
    @BindView(R.id.text_view_fantasy_name)
    TextView textViewFantasyName;
    @BindView(R.id.text_view_cpf_or_cnpj)
    TextView textViewCpfOrCnpj;
    @BindView(R.id.text_view_phone)
    TextView textViewPhone;
    @BindView(R.id.text_view_email)
    TextView textViewEmail;
    @BindView(R.id.text_view_customer_code)
    TextView textViewCustomerCode;

    CustomerListViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
