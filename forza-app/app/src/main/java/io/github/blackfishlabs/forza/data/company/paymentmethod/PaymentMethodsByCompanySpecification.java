package io.github.blackfishlabs.forza.data.company.paymentmethod;

import java.util.ArrayList;
import java.util.List;

import io.github.blackfishlabs.forza.data.realm.RealmResultsSpecification;
import io.github.blackfishlabs.forza.domain.entity.CompanyPaymentMethodEntity;
import io.github.blackfishlabs.forza.domain.entity.PaymentMethodEntity;
import io.realm.Realm;
import io.realm.RealmResults;

import static io.realm.Sort.ASCENDING;

public class PaymentMethodsByCompanySpecification implements RealmResultsSpecification<PaymentMethodEntity> {

    private final int companyId;

    public PaymentMethodsByCompanySpecification(final int companyId) {
        this.companyId = companyId;
    }

    @Override
    public RealmResults<PaymentMethodEntity> toRealmResults(final Realm realm) {
        RealmResults<CompanyPaymentMethodEntity> companyPaymentMethods = realm
                .where(CompanyPaymentMethodEntity.class)
                .equalTo(CompanyPaymentMethodEntity.Fields.COMPANY_ID, companyId)
                .findAll();

        List<Integer> paymentMethodIds = new ArrayList<>();
        for (CompanyPaymentMethodEntity paymentMethod : companyPaymentMethods) {
            paymentMethodIds.add(paymentMethod.getPaymentMethodId());
        }

        return realm.where(PaymentMethodEntity.class)
                .in(PaymentMethodEntity.Fields.PAYMENT_METHOD_ID,
                        paymentMethodIds.toArray(new Integer[]{paymentMethodIds.size()}))
                .sort(PaymentMethodEntity.Fields.DESCRIPTION, ASCENDING).findAll();
    }
}
