package io.github.blackfishlabs.forza.order.model;

public enum OrderStatus {

    STATUS_CREATED("Criado", 0),
    STATUS_MODIFIED("Modificado", 1),
    STATUS_SYNCED("Sincronizado", 2),
    STATUS_CANCELLED("Cancelado", 3),
    STATUS_INVOICED("Faturado", 5);

    private final String mDescription;

    private final int mOrdinalType;

    OrderStatus(final String description, final int ordinalType) {
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

    public static OrderStatus valueOf(int ordinalType) {
        for (OrderStatus status : values()) {
            if (status.getOrdinalType() == ordinalType) {
                return status;
            }
        }
        return null;
    }
}
