package org.intermine.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.util.Patterns;

import org.intermine.InterMineApplication;
import org.intermine.R;
import org.intermine.storage.Storage;
import org.intermine.util.Strs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class PreferencesFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String COMMA = ", ";

    @Inject
    Storage mStorage;

    private MultiSelectListPreference mMinesPreference;
    private EditTextPreference mAddMinePreference;

    private Set<String> mMineNames;

    // --------------------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] minesArray = getResources().getStringArray(R.array.mines_names);
        mMineNames = new HashSet<>(Arrays.asList(minesArray));

        InterMineApplication app = InterMineApplication.get(getActivity());
        app.inject(this);

        addPreferencesFromResource(R.xml.preferences);
        mMinesPreference = (MultiSelectListPreference) findPreference(Storage.MINE_NAMES_KEY);
        mAddMinePreference = (EditTextPreference) findPreference(Storage.ADD_MINE_KEY);
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

        mMinesPreference.setSummary(generateMinesSummary(mStorage.getMineNames()));
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (Storage.MINE_NAMES_KEY.equals(key)) {

            Set<String> mines = mStorage.getMineNames();

            if (mines.isEmpty()) {
                mStorage.setMineNames(new HashSet<>(mMineNames));
                mines = mMineNames;
                mMinesPreference.setValues(mines);
            }

            mMinesPreference.setSummary(generateMinesSummary(mines));
        } else if (Storage.ADD_MINE_KEY.equals(key)) {
            String potentialUrl = mAddMinePreference.getText();

            if (Patterns.WEB_URL.matcher(potentialUrl).matches()) {
                try {
                    URL url = new URL(potentialUrl);
                    Set<String> mines = mStorage.getMineNames();
                    mines.add(url.getHost());
                    mStorage.setMineNames(mines);
                } catch (MalformedURLException e) {
                }

                mAddMinePreference.setText("");
            }
        }
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected String generateMinesSummary(Set<String> mines) {
        return Strs.join(mines, COMMA);
    }
}
