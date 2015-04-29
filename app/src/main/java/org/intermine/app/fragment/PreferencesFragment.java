package org.intermine.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.intermine.app.InterMineApplication;
import org.intermine.app.R;
import org.intermine.app.activity.MinesActivity;
import org.intermine.app.storage.Storage;
import org.intermine.app.util.Strs;

import java.util.Set;

import javax.inject.Inject;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class PreferencesFragment extends PreferenceFragment {
    private static final String MINES_KEY = "mines";
    public static final String COMMA = ", ";

    @Inject
    Storage mStorage;

    private Preference mMinesPreference;

    // --------------------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InterMineApplication app = InterMineApplication.get(getActivity());
        app.inject(this);

        addPreferencesFromResource(R.xml.preferences);
        mMinesPreference = findPreference(MINES_KEY);
        mMinesPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), MinesActivity.class));
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMinesPreference.setSummary(generateMinesSummary(mStorage.getSelectedMineNames()));
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected String generateMinesSummary(Set<String> mines) {
        return Strs.join(mines, COMMA);
    }
}