package org.intermine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.intermine.R;
import org.intermine.core.Gene;
import org.intermine.core.List;
import org.intermine.fragment.GenesListFragment;
import org.intermine.listener.OnGeneSelectedListener;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GenesListActivity extends BaseActivity implements OnGeneSelectedListener {
    public static final String LIST_KEY = "list_key";

    // --------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------

    public static void start(Context context, org.intermine.core.List list) {
        Intent intent = new Intent(context, GenesListActivity.class);
        intent.putExtra(LIST_KEY, list);
        context.startActivity(intent);
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genes_list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List list = getIntent().getParcelableExtra(LIST_KEY);

        if (null != list) {
            setTitle(list.getTitle());

            GenesListFragment fragment = GenesListFragment.newInstance(list, "FlyMine");
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    @Override
    public void onGeneSelected(Gene gene) {

    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------
}
