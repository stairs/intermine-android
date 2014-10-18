package org.intermine.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import org.intermine.R;
import org.intermine.core.Gene;
import org.intermine.fragment.BrowseFragment;
import org.intermine.fragment.FavoritesFragment;
import org.intermine.fragment.GenesListFragment;
import org.intermine.fragment.InfoFragment;
import org.intermine.fragment.ListsFragment;
import org.intermine.fragment.NavigationDrawerFragment;
import org.intermine.fragment.SearchFragment;


public class MainActivity extends Activity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks, GenesListFragment.OnGeneSelectedListener {
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
                fragment = BrowseFragment.newInstance();
                break;
            case 2:
                fragment = ListsFragment.newInstance();
                break;
            case 3:
                fragment = FavoritesFragment.newInstance();
                break;
            case 4:
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSectionAttached(String title) {
        mTitle = title;
    }

    @Override
    public void onGeneSelected(Gene gene) {
        GeneViewActivity.start(this, gene);
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void populateContentFragment(Fragment fragment) {
        mMainContainer = (ViewGroup) findViewById(R.id.main_container);
        getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public NavigationDrawerFragment getNavigationDrawer() {
        return mNavigationDrawer;
    }
}
