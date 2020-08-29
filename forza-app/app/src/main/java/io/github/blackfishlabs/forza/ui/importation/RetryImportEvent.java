package io.github.blackfishlabs.forza.ui.importation;

import io.github.blackfishlabs.forza.domain.pojo.Company;

class RetryImportEvent {

    private final Company mCompany;

    private RetryImportEvent(final Company company) {
        mCompany = company;
    }

    static RetryImportEvent newEvent(final Company company) {
        return new RetryImportEvent(company);
    }

    public Company getCompany() {
        return mCompany;
    }
}
