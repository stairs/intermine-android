package org.intermine;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.intermine.storage.BaseStorage;
import org.intermine.storage.Storage;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class InterMineApplication extends Application {
    private Storage mStorage;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mStorage = new BaseStorage(preferences);
    }

    public Storage getStorage() {
        return mStorage;
    }
}
