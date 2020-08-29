package io.github.blackfishlabs.forza.helper;

import java.math.BigDecimal;

import static io.github.blackfishlabs.forza.helper.NumberUtils.CURRENCY_SCALE;
import static java.math.BigDecimal.ROUND_HALF_UP;

public class CurrencyUtils {

    private CurrencyUtils() {
    }

    public static double round(final double value) {
        BigDecimal bigValue = new BigDecimal(value);
        BigDecimal scaledValue = bigValue.setScale(CURRENCY_SCALE, ROUND_HALF_UP);
        return scaledValue.doubleValue();
    }
}
