package org.intermine.app.util;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

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

    public static Spannable spanWithBackgroundColor(String str, int start, int end, int color) {
        Spannable spannable = new SpannableString(str);
        spannable.setSpan(new BackgroundColorSpan(color),
                start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static Spannable spanWithBoldFont(String str, int start, int end, int color) {
        Spannable spannable = new SpannableString(str);
        spannable.setSpan(new ForegroundColorSpan(color),
                start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static String nullToEmpty(String string) {
        return (string == null) ? "" : string;
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
