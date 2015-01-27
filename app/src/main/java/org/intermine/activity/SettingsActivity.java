package org.intermine.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import org.intermine.R;
import org.intermine.dialog.AddMineDialogFragment;
import org.intermine.fragment.PreferencesFragment;

import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class SettingsActivity extends BaseActivity implements AddMineDialogFragment.AddMineDialogListener {
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(
                R.id.fragment_container, new PreferencesFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMineAdded(String mineName, String mineUrl) {
        Set<String> mines = getStorage().getCustomMineNames();

        if (!mines.contains(mineName)) {
            mines.add(mineName);
            mStorage.setCustomMineNames(mines);
            getStorage().setMineUrl(mineName, mineUrl);
        }
    }
}
