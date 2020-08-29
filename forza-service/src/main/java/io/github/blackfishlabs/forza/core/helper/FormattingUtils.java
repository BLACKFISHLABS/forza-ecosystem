package io.github.blackfishlabs.forza.core.helper;

import java.text.DecimalFormat;

import static com.google.common.base.Strings.isNullOrEmpty;

public class FormattingUtils {

    private FormattingUtils() {/* No constructor */}

    private static final DecimalFormat sCnpjFormatter = new DecimalFormat("00000000000000");
    private static final DecimalFormat sCpfFormatter = new DecimalFormat("00000000000");

    public static String formatCpforCnpj(String value) {
        if (isNullOrEmpty(value)) {
            return value;
        }

        value = getOnlyNumerics(value);

        final int valueSize = value.length();
        if (valueSize > 14) {
            return value;
        }

        boolean isCPF = valueSize < 12;
        DecimalFormat formatDecimal = isCPF ? sCpfFormatter : sCnpjFormatter;

        final String stringNumber = formatDecimal.format(Long.valueOf(value));

        return isCPF ? stringNumber.replaceAll(
                "([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "$1.$2.$3-$4")
                : stringNumber.replaceAll(
                "([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})",
                "$1.$2.$3/$4-$5");
    }

    private static String getOnlyNumerics(String str) {
        if (str == null) {
            return null;
        }

        StringBuilder strBuff = new StringBuilder();
        char c;

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);

            if (Character.isDigit(c)) {
                strBuff.append(c);
            }
        }
        return strBuff.toString();
    }
}
