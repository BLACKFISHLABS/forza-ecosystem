package io.github.blackfishlabs.forza.data.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.core.util.Pair;

import com.google.gson.Gson;

import io.github.blackfishlabs.forza.R;
import io.github.blackfishlabs.forza.domain.pojo.Company;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.Salesman;
import io.github.blackfishlabs.forza.domain.pojo.Settings;
import io.github.blackfishlabs.forza.helper.StringUtils;

import static android.text.TextUtils.isEmpty;

public class SettingsRepositoryImpl implements SettingsRepository {

    private static final String KEY_INITIAL_FLOW = "pref.initialFlow";
    private static final String KEY_LOGGED_USER = "pref.loggedUser";
    private static final String KEY_DEFAULT_COMPANY = "pref.defaultCompany";
    private static final String KEY_RUNNING_SYNC_PERIOD = "pref.runningSyncPeriod";
    private static final String KEY_LAST_SYNC_TIME = "pref.lastSyncTime";

    private final Context mContext;
    private final SharedPreferences mPreferences;
    private final Gson mGson;

    public SettingsRepositoryImpl(final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mContext = context;
        mGson = new Gson();
    }

    @Override
    public boolean isInitialFlowDone() {
        return mPreferences.getBoolean(KEY_INITIAL_FLOW, false);
    }

    @Override
    public void setInitialFlowDone() {
        mPreferences.edit().putBoolean(KEY_INITIAL_FLOW, true).apply();
    }

    @Override
    public boolean isAllSettingsPresent() {
        return !(isEmpty(getServerAddress())) && !(isEmpty(getAuthKey())) && !(isEmpty(getSyncPeriodicity()));
    }

    @Override
    public boolean isUserLoggedIn() {
        return mPreferences.contains(KEY_LOGGED_USER);
    }

    @Override
    public void setLoggedUser(final Salesman salesman, final Company defaultCompany) {
        final String userJson = mGson.toJson(salesman, Salesman.class);
        final String companyJson = mGson.toJson(defaultCompany, Company.class);

        Pair<String, String> pairJson = Pair.create(userJson, companyJson);

        mPreferences.edit().putString(KEY_LOGGED_USER, pairJson.first).putString(KEY_DEFAULT_COMPANY, pairJson.second).apply();
    }

    @Override
    public LoggedUser getLoggedUser() {
        final String loggedUserJson = mPreferences.getString(KEY_LOGGED_USER, null);
        final String defaultCompanyJson = mPreferences.getString(KEY_DEFAULT_COMPANY, null);

        if (StringUtils.isNullOrEmpty(loggedUserJson) || StringUtils.isNullOrEmpty(defaultCompanyJson)) {
            return null;
        }

        Salesman user = mGson.fromJson(loggedUserJson, Salesman.class);
        Company defaultCompany = mGson.fromJson(defaultCompanyJson, Company.class);

        return LoggedUser.create(user, defaultCompany);
    }

    @Override
    public Settings getSettings() {
        return Settings.create(getServerAddress(), getAuthKey(), getAutomaticallySyncOrders(), getSyncPeriodicityAsInt(), getChargeCode());
    }

    @Override
    public void setAutoSyncOrders(final boolean isEnabled) {
        mPreferences.edit().putBoolean(mContext.getString(R.string.settings_automatically_sync_orders_preference_key), isEnabled).apply();
    }

    @Override
    public boolean isRunningSyncWith(final long period) {
        return mPreferences.getLong(KEY_RUNNING_SYNC_PERIOD, 0) == period;
    }

    @Override
    public void setRunningSyncWith(final long syncPeriod) {
        mPreferences.edit().putLong(KEY_RUNNING_SYNC_PERIOD, syncPeriod).apply();
    }

    @Override
    public void setLastSyncTime(final String lastSyncTime) {
        mPreferences.edit().putString(KEY_LAST_SYNC_TIME, lastSyncTime).apply();
    }

    @Override
    public String getLastSyncTime() {
        return mPreferences.getString(KEY_LAST_SYNC_TIME, null);
    }


    private String getServerAddress() {
        return mPreferences.getString(mContext.getString(
                R.string.settings_server_address_preference_key), "");
    }

    private String getAuthKey() {
        return mPreferences.getString(mContext.getString(
                R.string.settings_auth_key_preference_key), "");
    }

    private boolean getAutomaticallySyncOrders() {
        return mPreferences.getBoolean(mContext.getString(
                R.string.settings_automatically_sync_orders_preference_key), true);
    }

    private int getSyncPeriodicityAsInt() {
        final String syncPeriodicity = getSyncPeriodicity();
        return TextUtils.isEmpty(syncPeriodicity) ? 0 : Integer.valueOf(syncPeriodicity);
    }

    private String getSyncPeriodicity() {
        return mPreferences.getString(mContext.getString(
                R.string.settings_sync_periodicity_preference_key), "");
    }

    private String getChargeCode() {
        return mPreferences.getString(mContext.getString(R.string.settings_charge_code_preference_key), "");
    }

    @Override
    public void setChargeCode(final String value) {
        mPreferences.edit().putString(mContext.getString(R.string.settings_charge_code_preference_key), value).apply();
    }

    @Override
    public void clearAllSettings() {
        mPreferences.edit().clear().apply();
    }
}
