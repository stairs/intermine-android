package org.intermine;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import org.intermine.module.AppModule;

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

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        objectGraph = ObjectGraph.create(new AppModule(this));
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public static InterMineApplication get(Context context) {
        return (InterMineApplication) context.getApplicationContext();
    }
}
