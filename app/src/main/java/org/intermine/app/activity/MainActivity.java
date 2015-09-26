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

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.intermine.app.R;
import org.intermine.app.core.Gene;
import org.intermine.app.core.List;
import org.intermine.app.core.templates.Template;
import org.intermine.app.fragment.GenesListFragment;
import org.intermine.app.fragment.InfoFragment;
import org.intermine.app.fragment.ListsFragment;
import org.intermine.app.fragment.LogInFragment;
import org.intermine.app.fragment.SearchFragment;
import org.intermine.app.fragment.TemplatesFragment;
import org.intermine.app.listener.OnGeneSelectedListener;

import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements OnGeneSelectedListener,
        ListsFragment.OnListSelectedListener, TemplatesFragment.OnTemplateSelectedListener {
    @InjectView(R.id.default_toolbar)
    protected Toolbar mToolbar;

    @InjectView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    @InjectView(R.id.navigation_view)
    protected NavigationView mNavigationView;

    @InjectView(R.id.mine_name)
    protected TextView mMineNameView;

    private List mGeneFavoritesList;

    protected CharSequence mTitle;
    private String mMineName;
    private boolean mMainMenuDisplayed;

    // --------------------------------------------------------------------------------------------
    // Activity Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.inject(this);

        populateContentFragment(SearchFragment.newInstance());
        initToolbar();
        setupDrawerLayout();

        mGeneFavoritesList = new List(getString(R.string.gene_favorites_list_name));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMineName = getStorage().getWorkingMineName();
        Set<String> mines = getStorage().getSelectedMineNames();

        if (!mines.contains(mMineName)) {
            mMineName = mines.iterator().next();
            getStorage().setWorkingMineName(mMineName);
        }
        mMineNameView.setText(mMineName);
    }

    private void setupDrawerLayout() {
        mMainMenuDisplayed = true;
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (mMainMenuDisplayed) {
                    Fragment fragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.drawer_search_all:
                            fragment = SearchFragment.newInstance();
                            break;
                        case R.id.drawer_templates:
                            fragment = TemplatesFragment.newInstance(mMineName);
                            break;
                        case R.id.drawer_lists:
                            fragment = ListsFragment.newInstance(mMineName);
                            break;
                        case R.id.drawer_favourites:
                            fragment = GenesListFragment.newInstance(mGeneFavoritesList, mMineName);
                            break;
                        case R.id.drawer_login:
                            fragment = LogInFragment.newInstance();
                            break;
                        case R.id.drawer_info:
                            fragment = InfoFragment.newInstance();
                            break;
                    }
                    populateContentFragment(fragment);
                    mDrawerLayout.closeDrawers();
                } else {
                    mMineName = menuItem.getTitle().toString();
                    getStorage().setWorkingMineName(mMineName);
                    mMineNameView.setText(mMineName);
                    onDrawerHeaderClick();
                }
                return true;
            }
        });
    }

    // --------------------------------------------------------------------------------------------
    // Event Listeners
    // --------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.drawer_header)
    public void onDrawerHeaderClick() {
        setMainMenuDisplayed(mMainMenuDisplayed);
        toggleExpandedView(mMainMenuDisplayed);

        mMainMenuDisplayed = !mMainMenuDisplayed;
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

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void populateContentFragment(Fragment fragment) {
        if (null != fragment) {
            getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        }
    }

    private void setMainMenuDisplayed(boolean displayed) {
        Menu menu = mNavigationView.getMenu();
        menu.setGroupVisible(R.id.main, !displayed);
        menu.setGroupVisible(R.id.rest, !displayed);

        if (displayed) {
            menu.removeGroup(R.id.mines);
            Set<String> mines = getStorage().getSelectedMineNames();
            int counter = 0;

            for (String mine : mines) {
                menu.add(R.id.mines, counter, counter, mine);
                counter++;
            }
        }
        menu.setGroupVisible(R.id.mines, displayed);
    }

    private void toggleExpandedView(boolean expanded) {
        if (expanded) {
            mMineNameView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.ic_expand_less_white_24dp), null);
        } else {
            mMineNameView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.ic_expand_more_white_24dp), null);
        }
    }
}