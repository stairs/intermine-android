package org.intermine.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.intermine.InterMineApplication;
import org.intermine.R;
import org.intermine.dialog.AddMineDialogFragment;
import org.intermine.storage.Storage;
import org.intermine.util.Strs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
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
    private Preference mAddMinePreference;

    private String[] mMineNamesArray;
    private Set<String> mMineNames;

    // --------------------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMineNamesArray = getResources().getStringArray(R.array.mines_names);
        mMineNames = new HashSet<>(Arrays.asList(mMineNamesArray));

        InterMineApplication app = InterMineApplication.get(getActivity());
        app.inject(this);

        addPreferencesFromResource(R.xml.preferences);
        mMinesPreference = (MultiSelectListPreference) findPreference(Storage.MINE_NAMES_KEY);
        updateMinesPreference();

        mAddMinePreference = findPreference(Storage.ADD_MINE_KEY);
        mAddMinePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AddMineDialogFragment.newInstance().show(getFragmentManager(),
                        AddMineDialogFragment.ADD_MINE_DIALOG_TAG);
                return false;
            }
        });
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
            mMinesPreference.setSummary(generateMinesSummary(mStorage.getMineNames()));
        } else if (key.startsWith(Storage.MINE_URL_KEY)) {
            String newMineName = key.substring(Storage.MINE_URL_KEY.length());

            Set<String> mines = mStorage.getMineNames();

            if (!mines.contains(newMineName)) {
                mines.add(newMineName);
                mStorage.setMineNames(mines);
            }

            updateMinesPreference();
            mMinesPreference.setValues(mStorage.getMineNames());
            mMinesPreference.setSummary(generateMinesSummary(mStorage.getMineNames()));
        }
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected String generateMinesSummary(Set<String> mines) {
        return Strs.join(mines, COMMA);
    }

    private void updateMinesPreference() {
        Set<String> customMines = mStorage.getCustomMineNames();
        CharSequence[] result = Arrays.copyOf(mMineNamesArray, mMineNamesArray.length + customMines.size());
        Iterator<String> iterator = customMines.iterator();

        for (int i = mMineNamesArray.length; i < mMineNamesArray.length + customMines.size(); i++) {
            result[i] = iterator.next();
        }
        mMinesPreference.setEntryValues(result);
        mMinesPreference.setEntries(result);
    }
}
