package org.intermine.app.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class Collections {
    private Collections() {
    }

    public static boolean isNullOrEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    /**
     * Creates a <i>mutable</i>, empty {@code ArrayList} instance.
     *
     * @return A new, empty {@code ArrayList}
     */
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    /**
     * Creates a <i>mutable</i>, empty {@code HashMap} instance.
     *
     * @return A new, empty {@code HashMap}
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }
}