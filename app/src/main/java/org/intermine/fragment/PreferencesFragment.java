package org.intermine.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.preference.PreferenceManager;

import org.intermine.R;
import org.intermine.activity.SettingsActivity;
import org.intermine.storage.Storage;
import org.intermine.util.Strs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class PreferencesFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String COMMA = ", ";

    private Preference mMinesPreference;

    private String[] mMinesNames;
    private String[] mDefaultMinesUrls;
    private String[] mMinesUrls;

    private Set<String> mDefaultMinesUrlsSet;

    // --------------------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMinesNames = getResources().getStringArray(R.array.mines_names);
        mMinesUrls = getResources().getStringArray(R.array.mines_urls);
        mDefaultMinesUrls = getResources().getStringArray(R.array.default_mines);
        mDefaultMinesUrlsSet = new HashSet<>(Arrays.asList(mDefaultMinesUrls));

        addPreferencesFromResource(R.xml.preferences);
        mMinesPreference = findPreference(Storage.MINE_NAMES_KEY);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mMinesPreference.setSummary(generateMinesSummary(getSelectedMinesNames(sharedPref)));
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (Storage.MINE_NAMES_KEY.equals(key)) {
            Set<String> mines = getSelectedMinesNames(sharedPreferences);

            if (mines.isEmpty()) {
                sharedPreferences.edit().putStringSet(Storage.MINE_NAMES_KEY, mDefaultMinesUrlsSet).apply();
                mines = getSelectedMinesNames(sharedPreferences);
            }

            mMinesPreference.setSummary(generateMinesSummary(mines));
        }
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected String generateMinesSummary(Set<String> mines) {
        return Strs.join(mines, COMMA);
    }

    private Set<String> getSelectedMinesNames(SharedPreferences sharedPreferences) {
        Set<String> minesUrls = sharedPreferences.getStringSet(Storage.MINE_NAMES_KEY, mDefaultMinesUrlsSet);
        return findMinesNamesByUrls(minesUrls);
    }

    private Set<String> findMinesNamesByUrls(Set<String> selectedMinesUrls) {
        Set<String> minesNames = new HashSet<String>();
        for (String mineUrl : selectedMinesUrls) {
            String mineName = findMineNameByUrl(mineUrl);
            minesNames.add(mineName);
        }
        return minesNames;
    }

    private String findMineNameByUrl(String url) {
        for (int i = 0; i < mMinesUrls.length; i++) {
            if (url.equals(mMinesUrls[i])) {
                return mMinesNames[i];
            }
        }
        return null;
    }
}
