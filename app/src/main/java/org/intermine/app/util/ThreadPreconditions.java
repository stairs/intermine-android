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

import android.os.Looper;

import org.intermine.app.BuildConfig;

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
