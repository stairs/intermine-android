package org.intermine.storage;

import android.content.SharedPreferences;

import org.intermine.util.Strs;

/**
 * @author Daria Komkova <Daria.Komkova @ hotmail.com>
 */
public class BaseStorage implements Storage {
    private SharedPreferences mPreferences;

    public BaseStorage(SharedPreferences preferences) {
        this.mPreferences = preferences;
    }

    @Override
    public String getUserToken(String mineName) {
        return mPreferences.getString(USER_TOKEN_KEY + mineName, Strs.EMPTY_STRING);
    }

    @Override
    public void setUserToken(String mineName, String token) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(USER_TOKEN_KEY + mineName, token);
        editor.commit();
    }
}
