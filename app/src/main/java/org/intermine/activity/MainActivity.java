package org.intermine.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.core.Gene;
import org.intermine.core.ListItems;
import org.intermine.core.templates.Template;
import org.intermine.fragment.FavoritesFragment;
import org.intermine.fragment.GenesListFragment;
import org.intermine.fragment.InfoFragment;
import org.intermine.fragment.ListsFragment;
import org.intermine.fragment.LogInFragment;
import org.intermine.fragment.NavigationDrawerFragment;
import org.intermine.fragment.SearchFragment;
import org.intermine.fragment.TemplatesFragment;
import org.intermine.net.request.get.GetTemplateResultsRequest;


public class MainActivity extends BaseActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks, GenesListFragment.OnGeneSelectedListener,
        ListsFragment.OnListSelectedListener, TemplatesFragment.OnTemplateSelectedListener {
    protected CharSequence mTitle;
    private NavigationDrawerFragment mNavigationDrawer;
    private ViewGroup mMainContainer;

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

        mNavigationDrawer.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), true);

        mTitle = getTitle();
    }

    // --------------------------------------------------------------------------------------------
    // Event Listeners
    // --------------------------------------------------------------------------------------------

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = SearchFragment.newInstance();
                break;
            case 1:
                fragment = TemplatesFragment.newInstance();
                break;
            case 2:
                fragment = ListsFragment.newInstance();
                break;
            case 3:
                fragment = FavoritesFragment.newInstance();
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
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.global, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void onSectionAttached(String title) {
        mTitle = title;
    }

    @Override
    public void onGeneSelected(Gene gene) {
        GeneViewActivity.start(this, gene);
    }

    @Override
    public void onListSelected(org.intermine.core.List list) {
        ListActivity.start(this, list);
    }

    @Override
    public void onTemplateSelected(Template template) {
        TemplateActivity.start(this, template);
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void populateContentFragment(Fragment fragment) {
        mMainContainer = (ViewGroup) findViewById(R.id.main_container);
        getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
    }

    public void restoreActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public NavigationDrawerFragment getNavigationDrawer() {
        return mNavigationDrawer;
    }
}
