package io.github.blackfishlabs.forza.data.company.customer;

import java.util.ArrayList;
import java.util.List;

import io.github.blackfishlabs.forza.data.realm.RealmResultsSpecification;
import io.github.blackfishlabs.forza.domain.entity.CompanyCustomerEntity;
import io.github.blackfishlabs.forza.domain.entity.CustomerEntity;
import io.github.blackfishlabs.forza.domain.pojo.CustomerStatus;
import io.github.blackfishlabs.forza.helper.StringUtils;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static io.realm.Sort.ASCENDING;

public class CustomersByCompanySpecification implements RealmResultsSpecification<CustomerEntity> {

    private final int mCompanyId;
    private int mByStatus = -1;
    private String mDocument;

    public CustomersByCompanySpecification(final int companyId) {
        this.mCompanyId = companyId;
    }

    public CustomersByCompanySpecification byStatus(@CustomerStatus int status) {
        mByStatus = status;
        return this;
    }

    public CustomersByCompanySpecification byDocument(String document) {
        mDocument = document;
        return this;
    }

    @Override
    public RealmResults<CustomerEntity> toRealmResults(final Realm realm) {
        RealmResults<CompanyCustomerEntity> companyCustomers = realm
                .where(CompanyCustomerEntity.class)
                .equalTo(CompanyCustomerEntity.Fields.COMPANY_ID, mCompanyId)
                .findAll();

        List<Integer> customerIds = new ArrayList<>();
        for (CompanyCustomerEntity customer : companyCustomers) {
            customerIds.add(customer.getCustomerId());
        }

        RealmQuery<CustomerEntity> query = realm
                .where(CustomerEntity.class)
                .in(CustomerEntity.Fields.ID, customerIds.toArray(new Integer[]{customerIds.size()}));

        if (mByStatus != -1) {
            query.equalTo(CustomerEntity.Fields.STATUS, mByStatus);
        }

        if (!StringUtils.isNullOrEmpty(mDocument)) {
            query.equalTo(CustomerEntity.Fields.CNPJCPF, mDocument);
        }

        return query.sort(CustomerEntity.Fields.NAME, ASCENDING).findAll();
    }
}
