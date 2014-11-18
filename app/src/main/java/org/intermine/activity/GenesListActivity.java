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
    public static final String MINE_NAME_KEY = "mine_name_key";

    private List mList;
    private String mMineName;

    // --------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------

    public static void start(Context context, org.intermine.core.List list, String mineName) {
        Intent intent = new Intent(context, GenesListActivity.class);
        intent.putExtra(LIST_KEY, list);
        intent.putExtra(MINE_NAME_KEY, mineName);
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

        mList = getIntent().getParcelableExtra(LIST_KEY);
        mMineName = getIntent().getStringExtra(MINE_NAME_KEY);

        if (null != mList) {
            setTitle(mList.getTitle());

            GenesListFragment fragment = GenesListFragment.newInstance(mList, mMineName);
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
