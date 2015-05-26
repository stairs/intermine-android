package org.intermine.app;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.app.Application;
import android.content.Context;

import org.intermine.app.module.AppModule;

import dagger.ObjectGraph;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class InterMineApplication extends Application {
    /**
     * Dagger object graph.
     */
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new AppModule(this));
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public static InterMineApplication get(Context context) {
        return (InterMineApplication) context.getApplicationContext();
    }
}
