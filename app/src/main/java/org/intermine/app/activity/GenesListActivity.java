package org.intermine.app.activity;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.intermine.app.R;
import org.intermine.app.core.Gene;
import org.intermine.app.core.List;
import org.intermine.app.fragment.GenesListFragment;
import org.intermine.app.listener.OnGeneSelectedListener;
import org.intermine.app.util.Strs;
import org.intermine.app.util.Uris;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GenesListActivity extends BaseActivity implements OnGeneSelectedListener {
    public static final String LIST_KEY = "list_key";
    public static final String MINE_NAME_KEY = "mine_name_key";

    private static final String BAG_NAME_PARAM = "bagName";

    private List mList;
    private String mMineName;

    // --------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------

    public static void start(Context context, org.intermine.app.core.List list, String mineName) {
        Intent intent = new Intent(context, GenesListActivity.class);
        intent.putExtra(LIST_KEY, list);
        intent.putExtra(MINE_NAME_KEY, mineName);
        context.startActivity(intent);
    }

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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.open_in_browser_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_in_browser:
                String url = mStorage.getMineWebAppUrl(mMineName);
                String title = "List Analysis";

                if (!Strs.isNullOrEmpty(mList.getName())) {
                    title = mList.getName();
                }

                if (mList.isAuthorized()) {
                    url += "/login.do";
                } else {
                    url = generateListAnalysisPath(url, mList, mMineName);
                }
                WebActivity.start(this, title, url);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onGeneSelected(Gene gene) {

    }
    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    private String generateListAnalysisPath(String baseUrl, List list, String mineName) {
        Map<String, String> params = new HashMap<>();
        params.put(BAG_NAME_PARAM, mList.getName());
        return Uris.expandQuery(baseUrl + getString(R.string.list_analysis_path), params);
    }
}
