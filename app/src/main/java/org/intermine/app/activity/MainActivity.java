package org.intermine.app.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import org.intermine.app.R;
import org.intermine.app.core.Gene;
import org.intermine.app.core.List;
import org.intermine.app.core.templates.Template;
import org.intermine.app.fragment.GenesListFragment;
import org.intermine.app.fragment.InfoFragment;
import org.intermine.app.fragment.ListsFragment;
import org.intermine.app.fragment.LogInFragment;
import org.intermine.app.fragment.NavigationDrawerFragment;
import org.intermine.app.fragment.SearchFragment;
import org.intermine.app.fragment.TemplatesFragment;
import org.intermine.app.listener.OnGeneSelectedListener;

import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks, OnGeneSelectedListener,
        ListsFragment.OnListSelectedListener, TemplatesFragment.OnTemplateSelectedListener {
    private NavigationDrawerFragment mNavigationDrawer;

    private List mGeneFavoritesList;

    protected CharSequence mTitle;

    // --------------------------------------------------------------------------------------------
    // Activity Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);

        mNavigationDrawer = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        DrawerLayout drawer = ButterKnife.findById(this, R.id.drawer_layout);
        mNavigationDrawer.setUp(R.id.navigation_drawer, drawer, true);

        mGeneFavoritesList = new List(getString(R.string.gene_favorites_list_name));
    }

    // --------------------------------------------------------------------------------------------
    // Event Listeners
    // --------------------------------------------------------------------------------------------

    @Override
    public void onNavigationDrawerItemSelected(int position, String mineName) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = SearchFragment.newInstance();
                break;
            case 1:
                fragment = TemplatesFragment.newInstance(mineName);
                break;
            case 2:
                fragment = ListsFragment.newInstance(mineName);
                break;
            case 3:
                fragment = GenesListFragment.newInstance(mGeneFavoritesList, mineName);
                break;
            case 4:
                fragment = LogInFragment.newInstance();
                break;
            case 5:
                fragment = InfoFragment.newInstance();
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawer.isDrawerOpen()) {
            restoreActionBar();
            return super.onCreateOptionsMenu(menu);
        }
        return true;
    }

    public void onSectionAttached(String title) {
        mTitle = title;
    }

    @Override
    public void onGeneSelected(Gene gene) {
        GeneViewActivity.start(this, gene);
    }

    @Override
    public void onListSelected(org.intermine.app.core.List list, String mineName) {
        GenesListActivity.start(this, list, mineName);
    }

    @Override
    public void onTemplateSelected(Template template, String mineName) {
        TemplateActivity.start(this, template, mineName);
    }

    @Override
    public void onDialogDismissed(int code) {
        if (UNAUTHORIZED_CODE == code) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = LogInFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
            setTitle(getString(R.string.log_in));
        }
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void populateContentFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public NavigationDrawerFragment getNavigationDrawer() {
        return mNavigationDrawer;
    }
}