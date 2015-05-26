package org.intermine.app.fragment;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.intermine.app.R;
import org.intermine.app.adapter.ExpandableMenuListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NavigationDrawerFragment extends BaseFragment {
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private NavigationDrawerCallbacks mCallbacks;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerListView;
    private ExpandableMenuListAdapter mAdapter;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private boolean mDrawerIconRequried;

    public NavigationDrawerFragment() {
    }

    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position, String mineName);
    }

    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = getStorage().hasUserLearnedDrawer();

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        } else {
            selectItem(mCurrentSelectedPosition, null);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = (ExpandableListView) inflater.inflate(
                R.layout.navigation_drawer_fragment, container, false);
        return mDrawerListView;
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    public void setUp(int fragmentId, DrawerLayout drawerLayout, boolean displayDrawerIcon) {
        mAdapter = new ExpandableMenuListAdapter(getActivity());
        mAdapter.updateMenuList(generateMenu());

        mDrawerListView.setDividerHeight(0);
        mDrawerListView.setChildDivider(getResources().getDrawable(android.R.color.transparent));
        mDrawerListView.setAdapter(mAdapter);
        mDrawerListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (0 == mAdapter.getChildrenCount(groupPosition)) {
                    selectItem(groupPosition, null);
                }
                return false;
            }
        });
        mDrawerListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                selectItem(groupPosition, (String) mAdapter.getChild(groupPosition, childPosition));
                return false;
            }
        });
        //mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerIconRequried = displayDrawerIcon;

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                mAdapter.updateMenuList(generateMenu());

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    getStorage().setUserLearnedDrawer(true);
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        if (null != mDrawerLayout) {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
                mDrawerLayout.openDrawer(mFragmentContainerView);
            }

            // Defer code dependent on restoration of previous instance state.
            mDrawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    mDrawerToggle.syncState();
                }
            });

            mDrawerLayout.setDrawerListener(mDrawerToggle);
            mDrawerToggle.setDrawerIndicatorEnabled(displayDrawerIcon);
        } else {
            getActionBarActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void selectItem(int position, String mineName) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            //mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position, mineName);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (null != mDrawerLayout) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null) {
            if (isDrawerOpen()) {
                showGlobalContextActionBar();
            } else {
                mDrawerToggle.setDrawerIndicatorEnabled(mDrawerIconRequried);
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (null != mDrawerLayout && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.search_action) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    private Map<String, List<String>> generateMenu() {
        Set<String> authMines = getStorage().getMineToUserTokenMap().keySet();
        List<String> favoritesSubmenus = new ArrayList<>(authMines);
        List<String> minesSubmenus = new ArrayList<>(getStorage().getSelectedMineNames());
        List<String> emptyList = java.util.Collections.emptyList();

        LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
        map.put(getString(R.string.search), emptyList);
        map.put(getString(R.string.templates), minesSubmenus);
        map.put(getString(R.string.lists), minesSubmenus);
        map.put(getString(R.string.favorites), favoritesSubmenus);
        map.put(getString(R.string.log_in), emptyList);
        map.put(getString(R.string.info), emptyList);
        return map;
    }

    private ActionBar getActionBar() {
        return getActionBarActivity().getSupportActionBar();
    }

    private ActionBarActivity getActionBarActivity() {
        return (ActionBarActivity) getActivity();
    }
}