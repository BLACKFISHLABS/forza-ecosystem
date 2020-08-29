package io.github.blackfishlabs.forza.domain.pojo;

public class Settings {

    private final String serverAddress;

    private final String authKey;

    private final boolean automaticallySyncOrders;

    private final int syncPeriodicity;

    private final String chargeCode;

    private Settings(final String serverAddress, final String authKey, final boolean automaticallySyncOrders, final int syncPeriodicity, final String chargeCode) {
        this.serverAddress = serverAddress;
        this.authKey = authKey;
        this.automaticallySyncOrders = automaticallySyncOrders;
        this.syncPeriodicity = syncPeriodicity;
        this.chargeCode = chargeCode;
    }

    public static Settings create(final String serverAddress, final String authKey, final boolean automaticallySyncOrders, final int syncPeriodicity, final String chargeCode) {
        return new Settings(serverAddress, authKey, automaticallySyncOrders, syncPeriodicity, chargeCode);
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getAuthKey() {
        return authKey;
    }

    public boolean isAutomaticallySyncOrders() {
        return automaticallySyncOrders;
    }

    public int getSyncPeriodicity() {
        return syncPeriodicity;
    }

    public String getChargeCode() {
        return chargeCode;
    }
}
