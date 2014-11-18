package org.intermine.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import java.util.Iterator;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

@SuppressLint("DefaultLocale")
public class Strs {
    public final static String EMPTY_STRING = "";

    public final static String TIMER_FORMAT = "%02d:%02d:%02d";
    public final static String TIME_UNIT_FORMAT = "%02d";
    public final static String HOURS_FORMAT = "%02d:%02d";
    public final static String HOURS_FORMAT_WITHOUT_COLON = "%02d%02d";
    public final static String HOURS_HUMAN_FORMAT = "%02d %s %02d %s";
    public final static String MINS_HUMAN_FORMAT = "%02d %s";

    public final static double MIN_DISTANCE_IN_KM = 0.1;

    /**
     * Formats time in milliseconds to given format
     *
     * @param millis Time in milliseconds
     * @return Formatted time string
     */
    public static String formatTime(long millis) {
        return String.format(TIMER_FORMAT, MILLISECONDS.toHours(millis),
                MILLISECONDS.toMinutes(millis) - HOURS.toMinutes(MILLISECONDS.toHours(millis)),
                MILLISECONDS.toSeconds(millis) - MINUTES.toSeconds(MILLISECONDS.toMinutes(millis)));
    }

    /**
     * Formats time in milliseconds to minutes string
     *
     * @param millis Time in milliseconds
     * @return Formatted minutes
     */
    public static String formatMinutes(long millis) {
        return Long.toString(MILLISECONDS.toMinutes(millis));
    }

    /**
     * Formats time in milliseconds to minutes string
     *
     * @param millis Time in milliseconds
     * @return Formatted minutes
     */
    public static String formatMinutesRound(long millis) {
        return Long.toString(Math.round(millis * 1.0 / MINUTES.toMillis(1)));
    }


    public static String formatHours(long millis) {
        return String.format(TIME_UNIT_FORMAT, MILLISECONDS.toHours(millis));
    }

    /**
     * Formats time in milliseconds to hours string according to passed format.
     *
     * @param millis Time in milliseconds
     * @param format Time pattern
     * @return Formatted hours
     */
    public static String formatHours(String format, long millis) {
        return String.format(format, MILLISECONDS.toHours(millis),
                MILLISECONDS.toMinutes(millis) - HOURS.toMinutes(MILLISECONDS.toHours(millis)));
    }

    public static String formatMinutesForTimer(long millis) {
        return String.format(TIME_UNIT_FORMAT,
                MILLISECONDS.toMinutes(millis) - HOURS.toMinutes(MILLISECONDS.toHours(millis)));
    }


    /**
     * Returns string in Yandex-Like style, with red initial letter
     *
     * @param str String to brandize
     * @return Brandized spanned string
     */
    public static Spanned brandize(String str) {
        String html = "<font color='red'>" + str.substring(0, 1) + "</font>"
                + str.substring(1, str.length());

        return Html.fromHtml(html);
    }

    /**
     * Returns string in Yandex-Like style, with red initial letter
     *
     * @param ctx Context
     * @param res Link to string resource
     * @return Brandized spanned string
     */
    public static Spanned brandize(Context ctx, int res) {
        String str = ctx.getResources().getString(res);
        return brandize(str);
    }

    /**
     * Draws string in red
     *
     * @param str String to colorize
     * @return Colorized string
     */
    public static Spanned redize(String str) {
        return Html.fromHtml("<font color='red'>" + str + "</font>");
    }

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
