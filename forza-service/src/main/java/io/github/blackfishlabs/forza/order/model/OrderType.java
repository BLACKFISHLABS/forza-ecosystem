package io.github.blackfishlabs.forza.order.model;

public enum OrderType {

    ORDER_TYPE_NORMAL("Normal", 0),
    ORDER_TYPE_RETURNED("Devolução", 1);

    private final String mDescription;

    private final int mOrdinalType;

    OrderType(final String description, final int ordinalType) {
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

    public static OrderType valueOf(int ordinalType) {
        for (OrderType type : values()) {
            if (type.getOrdinalType() == ordinalType) {
                return type;
            }
        }
        return null;
    }
}
