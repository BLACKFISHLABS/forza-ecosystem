package io.github.blackfishlabs.forza.charge.model;

public enum ChargeStatus {

    STATUS_CREATED("Criado", 0),
    STATUS_SYNCED("Sincronizado", 1),
    STATUS_FINISHED("Concluido", 2);

    private final String mDescription;

    private final int mOrdinalType;

    ChargeStatus(final String description, final int ordinalType) {
        mDescription = description;
        mOrdinalType = ordinalType;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getOrdinalType() {
        return mOrdinalType;
    }

    @Override
    public String toString() {
        return getDescription();
    }

    public static ChargeStatus valueOf(int ordinalType) {
        for (ChargeStatus status : values()) {
            if (status.getOrdinalType() == ordinalType) {
                return status;
            }
        }
        return null;

    }
}