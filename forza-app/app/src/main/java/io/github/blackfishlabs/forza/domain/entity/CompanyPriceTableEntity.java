package io.github.blackfishlabs.forza.domain.entity;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class CompanyPriceTableEntity implements RealmModel {

    public static final class Fields {
        public static final String COMPANY_ID = "companyId";
    }

    @PrimaryKey
    private String id;

    @Required
    private Integer companyId;

    @Required
    private Integer priceTableId;

    public CompanyPriceTableEntity withId(Integer companyId, Integer priceTableId) {
        this.id = String.valueOf(companyId) + priceTableId;
        this.companyId = companyId;
        this.priceTableId = priceTableId;
        return this;
    }

    public String getId() {
        return id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public Integer getPriceTableId() {
        return priceTableId;
    }
}
