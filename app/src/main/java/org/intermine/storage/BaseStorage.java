package org.intermine.storage;

import android.content.Context;
import android.content.SharedPreferences;

import org.intermine.InterMineApplication;
import org.intermine.R;
import org.intermine.util.Strs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

/**
 * @author Daria Komkova <Daria.Komkova @ hotmail.com>
 */
public abstract class BaseStorage implements Storage {
    @Inject
    SharedPreferences mPreferences;

    private Context mContext;

    private Set<String> mDefaultMineNames;

    public BaseStorage(Context ctx) {
        mContext = ctx;

        InterMineApplication app = InterMineApplication.get(ctx);
        app.inject(this);

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
    public Set<String> getCustomMineNames() {
        return mPreferences.getStringSet(CUSTOM_MINE_NAMES_KEY, new HashSet<String>());
    }

    @Override
    public void setCustomMineNames(Set<String> mineNames) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putStringSet(CUSTOM_MINE_NAMES_KEY, new HashSet<>(mineNames));
        editor.commit();
    }

    @Override
    public Set<String> getMineNames() {
        return mPreferences.getStringSet(MINE_NAMES_KEY, mDefaultMineNames);
    }

    @Override
    public void setMineNames(Set<String> mineNames) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putStringSet(MINE_NAMES_KEY, new HashSet<>(mineNames));
        editor.commit();
    }

    @Override
    public String getMineUrl(String mine) {
        return mPreferences.getString(MINE_URL_KEY + mine, Strs.EMPTY_STRING);
    }

    @Override
    public void setMineUrl(String mine, String url) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(MINE_URL_KEY + mine, url);
        editor.commit();
    }

    protected Context getContext() {
        return mContext;
    }
}
