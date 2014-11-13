package org.intermine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import org.intermine.R;
import org.intermine.fragment.PreferencesFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class SettingsActivity extends ActionBarActivity {
    @InjectView(R.id.default_toolbar)
    Toolbar mToolbar;

    // --------------------------------------------------------------------------------------------
    // Activity Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);

        getFragmentManager().beginTransaction().replace(
                R.id.fragment_container, new PreferencesFragment()).commit();
    }
}
