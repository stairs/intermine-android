package org.intermine.activity;

import android.app.Activity;
import android.os.Bundle;

import org.intermine.fragment.PreferencesFragment;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class SettingsActivity extends Activity {
    // --------------------------------------------------------------------------------------------
    // Activity Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(
                android.R.id.content, new PreferencesFragment()).commit();
    }
}
