package org.intermine.storage;

import android.content.Context;
import android.content.SharedPreferences;

import org.intermine.R;
import org.intermine.util.Strs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daria Komkova <Daria.Komkova @ hotmail.com>
 */
public class BaseStorage implements Storage {
    private SharedPreferences mPreferences;
    private Context mContext;

    private Set<String> mDefaultMineNames;

    public BaseStorage(SharedPreferences preferences, Context ctx) {
        this.mPreferences = preferences;
        mContext = ctx;

        String[] mineNamesArr = ctx.getResources().getStringArray(R.array.mines_names);
        mDefaultMineNames = new HashSet<>(Arrays.asList(mineNamesArr));
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

    @Override
    public Set<String> getMineNames() {
        return mPreferences.getStringSet(MINE_NAMES_KEY, mDefaultMineNames);
    }

    @Override
    public void setMineNames(Set<String> mineNames) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putStringSet(MINE_NAMES_KEY, mineNames);
        editor.commit();
    }
}
