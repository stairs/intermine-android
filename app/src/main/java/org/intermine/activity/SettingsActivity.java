package org.intermine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import org.intermine.InterMineApplication;
import org.intermine.R;
import org.intermine.dialog.AddMineDialogFragment;
import org.intermine.fragment.PreferencesFragment;
import org.intermine.storage.Storage;

import java.util.Set;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class SettingsActivity extends ActionBarActivity implements AddMineDialogFragment.AddMineDialogListener {
    @Inject
    Storage mStorage;

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

        InterMineApplication app = InterMineApplication.get(this);
        app.inject(this);

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
        Set<String> mines = mStorage.getMineNames();

        if (!mines.contains(mineName)) {
            mines.add(mineName);
            mStorage.setMineNames(mines);
            Set<String> selectedMinesNames = mStorage.getSelectedMineNames();
            selectedMinesNames.add(mineName);
            mStorage.setSelectedMineNames(selectedMinesNames);

            mStorage.setMineUrl(mineName, mineUrl);
        }
    }
}
