package org.intermine.storage;

import android.content.SharedPreferences;

/**
 * @author Daria Komkova <Daria.Komkova @ hotmail.com>
 */
public class BaseStorage implements Storage {
    private SharedPreferences mPreferences;

    public BaseStorage(SharedPreferences preferences){
        this.mPreferences = preferences;
    }

    @Override
    public float getProjectCoefficient(String projectId) {
        return mPreferences.getFloat(projectId, 1.0f);
    }

    @Override
    public void setProjectCoefficient(String projectId, float coefficient) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putFloat(projectId, coefficient);
        editor.commit();
    }
}
