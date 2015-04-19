package org.intermine.app.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.intermine.app.InterMineApplication;
import org.intermine.app.R;
import org.intermine.app.dialog.AddMineDialogFragment;
import org.intermine.app.storage.Storage;
import org.intermine.app.util.Strs;

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

    // --------------------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InterMineApplication app = InterMineApplication.get(getActivity());
        app.inject(this);

        addPreferencesFromResource(R.xml.preferences);
        mMinesPreference = (MultiSelectListPreference) findPreference(Storage.SELECTED_MINE_NAMES_KEY);
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
        mMinesPreference.setSummary(generateMinesSummary(mStorage.getSelectedMineNames()));
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (Storage.SELECTED_MINE_NAMES_KEY.equals(key)) {
            Set<String> selectedMines = mStorage.getSelectedMineNames();

            if (selectedMines.isEmpty()) {
                selectedMines = mStorage.getMineNames();
                mStorage.setSelectedMineNames(selectedMines);
            }
            mMinesPreference.setValues(selectedMines);

            mMinesPreference.setSummary(generateMinesSummary(selectedMines));
        } else {
            updateMinesPreference();

            Set<String> selectedMines = mStorage.getSelectedMineNames();
            mMinesPreference.setValues(selectedMines);

            mMinesPreference.setSummary(generateMinesSummary(selectedMines));
        }
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected String generateMinesSummary(Set<String> mines) {
        return Strs.join(mines, COMMA);
    }

    private void updateMinesPreference() {
        Set<String> mines = mStorage.getMineNames();
        CharSequence[] result = new CharSequence[mines.size()];
        Iterator<String> iterator = mines.iterator();

        for (int i = 0; i < mines.size(); i++) {
            result[i] = iterator.next();
        }
        mMinesPreference.setEntryValues(result);
        mMinesPreference.setEntries(result);
    }
}
