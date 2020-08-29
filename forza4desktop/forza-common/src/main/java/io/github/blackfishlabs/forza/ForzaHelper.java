package io.github.blackfishlabs.forza;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForzaHelper {

    public static String validation(String nfe) {
        String json = nfe.replaceAll("#", "\"");

        json = cleanText(json, "\r", "\n");

        json = json.replaceAll("\\s+", " ").trim();
        return json;
    }

    private static String cleanText(String text, String... dirty) {
        for (String d : dirty) {
            text = text.replaceAll(d, "");
        }
        return text;
    }

    public static String toDirFormat(final Date date) {
        Locale locale = Locale.getDefault();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM", locale);
        return date == null ? "" : sdf.format(date);
    }

}
