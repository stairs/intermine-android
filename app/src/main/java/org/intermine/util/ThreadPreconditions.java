package org.intermine.util;

import android.os.Looper;

import org.intermine.BuildConfig;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class ThreadPreconditions {
    private ThreadPreconditions() {
    }

    public static void checkOnMainThread() {
        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                throw new IllegalStateException("This method should be called from the Main Thread");
            }
        }
    }
}
