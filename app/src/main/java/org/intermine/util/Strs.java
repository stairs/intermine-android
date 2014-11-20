package org.intermine.util;

import java.util.Iterator;

public class Strs {
    public final static String EMPTY_STRING = "";
    public final static String BR = "<br/>";

    /**
     * Capitalizes first letter of a string
     *
     * @param str String to capitalize
     * @return Capitalized string
     */
    public static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * Strip the text from end <br/> tags.
     *
     * @param str String to strip
     * @return Stripped string
     */
    public static String stripFromBr(String str) {
        String trimmed = nullToEmpty(str).trim();

        if (trimmed.endsWith(BR)) {
            return trimmed.substring(0, trimmed.length() - BR.length()).trim();
        }
        return trimmed;
    }

    /**
     * Mask the string
     *
     * @param str     String to be masked
     * @param visible Count of last character that should stay unmasked
     * @return Masked string
     */
    public static String shadow(String str, int visible) {
        String cipherNumber = "";

        for (int i = 0; i < str.length(); i++) {
            if (i < str.length() - visible - 1) {
                cipherNumber += "*";
            } else {
                cipherNumber += str.charAt(i);
            }

            if (0 == (i + 1) % 4) {
                cipherNumber += " ";
            }
        }

        return cipherNumber;
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static String nullToEmpty(String string) {
        return (string == null) ? "" : string;
    }

    public static String emptyToNull(String string) {
        return isNullOrEmpty(string) ? null : string;
    }

    /**
     * Joins array of object to single string by separator
     *
     * @param iterable  any kind of iterable ex.: <code>["a", "b", "c"]</code>
     * @param separator separetes entries ex.: <code>","</code>
     * @return joined string ex.: <code>"a,b,c"</code>
     */
    public static String join(Iterable<?> iterable, String separator) {
        Iterator<?> oIter;
        if (iterable == null || (!(oIter = iterable.iterator()).hasNext()))
            return "";
        StringBuilder oBuilder = new StringBuilder(String.valueOf(oIter.next()));
        while (oIter.hasNext())
            oBuilder.append(separator).append(oIter.next());
        return oBuilder.toString();
    }
}
