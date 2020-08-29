package io.github.blackfishlabs.forza.data.settings;

import io.github.blackfishlabs.forza.domain.pojo.Company;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.Salesman;
import io.github.blackfishlabs.forza.domain.pojo.Settings;

public interface SettingsRepository {

    boolean isInitialFlowDone();

    void setInitialFlowDone();

    boolean isAllSettingsPresent();

    boolean isUserLoggedIn();

    void setLoggedUser(Salesman user, Company defaultCompany);

    LoggedUser getLoggedUser();

    Settings getSettings();

    void setAutoSyncOrders(final boolean isEnabled);

    boolean isRunningSyncWith(final long period);

    void setRunningSyncWith(final long syncPeriod);

    void setLastSyncTime(String lastUpdateTime);

    String getLastSyncTime();

    void setChargeCode(final String value);

    void clearAllSettings();
}
