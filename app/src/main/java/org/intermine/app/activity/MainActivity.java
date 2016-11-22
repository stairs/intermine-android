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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.intermine.app.R;
import org.intermine.app.core.Gene;
import org.intermine.app.core.templates.Template;
import org.intermine.app.fragment.FavoritesListFragment;
import org.intermine.app.fragment.InfoFragment;
import org.intermine.app.fragment.ListsFragment;
import org.intermine.app.fragment.LogInFragment;
import org.intermine.app.fragment.SearchFragment;
import org.intermine.app.fragment.TemplatesFragment;
import org.intermine.app.listener.OnGeneSelectedListener;
import org.intermine.app.util.Strs;

import java.lang.reflect.Field;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class MainActivity extends BaseActivity implements OnGeneSelectedListener,
        ListsFragment.OnListSelectedListener, TemplatesFragment.OnTemplateSelectedListener {
    public static final int LOGIN_ACTIVITY_CODE = 0x3435;

    @BindView(R.id.default_toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    protected NavigationView mNavigationView;

    protected TextView mMineNameView;

    protected CharSequence mTitle;
    private String mMineName;
    private boolean mMainMenuDisplayed;
    private int mLastSelectedMenuItem;

    // --------------------------------------------------------------------------------------------
    // Activity Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.InterMine_Theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        if (null == savedInstanceState) {
            populateContentFragment(SearchFragment.newInstance(false));
        }
        initToolbar();

        View header = mNavigationView.getHeaderView(0);
        mMineNameView = (TextView) header.findViewById(R.id.mine_title);

        setupDrawerLayout();
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

    // --------------------------------------------------------------------------------------------
    // Event Listeners
    // --------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setMainMenuDisplayed(false);
                toggleExpandedView(false);

                mMainMenuDisplayed = true;
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Optional
    @OnClick(R.id.drawer_header)
    public void onDrawerHeaderClick() {
        setMainMenuDisplayed(mMainMenuDisplayed);
        toggleExpandedView(mMainMenuDisplayed);

        mMainMenuDisplayed = !mMainMenuDisplayed;
    }

    public void onSectionAttached(String title) {
        mTitle = title;
        setTitle(mTitle);
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
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = LogInFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
            setTitle(getString(R.string.log_in));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLoginState();
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

    private void setupDrawerLayout() {
        mMainMenuDisplayed = true;
        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                            @Override
                                            public void onDrawerClosed(View drawerView) {
                                                if (mMainMenuDisplayed) {
                                                    if (R.id.drawer_settings == mLastSelectedMenuItem) {
                                                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                                        startActivity(intent);
                                                        mLastSelectedMenuItem = -1;
                                                    } else if (R.id.drawer_login == mLastSelectedMenuItem) {
                                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                                        startActivityForResult(intent, LOGIN_ACTIVITY_CODE);
                                                        mLastSelectedMenuItem = -1;
                                                    } else if (R.id.drawer_logout == mLastSelectedMenuItem) {
                                                        getStorage().setUserToken(mMineName, null);

                                                        Menu menu = mNavigationView.getMenu();
                                                        menu.findItem(R.id.drawer_login).setVisible(true);
                                                        menu.findItem(R.id.drawer_logout).setVisible(false);
                                                        updateNavigationView();
                                                        mLastSelectedMenuItem = -1;
                                                    }
                                                }
                                            }
                                        }

        );
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()

                                                          {
                                                              @Override
                                                              public boolean onNavigationItemSelected(MenuItem menuItem) {
                                                                  if (mMainMenuDisplayed) {
                                                                      populateMainContent(menuItem.getItemId());
                                                                      mLastSelectedMenuItem = menuItem.getItemId();
                                                                  } else {
                                                                      mMineName = menuItem.getTitle().toString();
                                                                      getStorage().setWorkingMineName(mMineName);
                                                                      mMineNameView.setText(mMineName);
                                                                      updateLoginState();

                                                                      if (menuItem.getItemId() != R.id.drawer_settings &&
                                                                              menuItem.getItemId() != R.id.drawer_login &&
                                                                              menuItem.getItemId() != R.id.drawer_logout) {
                                                                          populateMainContent(mLastSelectedMenuItem);
                                                                      }
                                                                  }
                                                                  mDrawerLayout.closeDrawers();
                                                                  return true;
                                                              }
                                                          }

        );
    }

    protected void populateContentFragment(Fragment fragment) {
        if (null != fragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
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

    private void populateMainContent(int menuItemId) {
        Fragment fragment = null;

        switch (menuItemId) {
            case R.id.drawer_search_all:
                fragment = SearchFragment.newInstance(true);
                break;
            case R.id.drawer_templates:
                fragment = TemplatesFragment.newInstance(mMineName);
                break;
            case R.id.drawer_lists:
                fragment = ListsFragment.newInstance(mMineName);
                break;
            case R.id.drawer_favourites:
                fragment = FavoritesListFragment.newInstance();
                break;
            case R.id.drawer_info:
                fragment = InfoFragment.newInstance();
                break;
        }
        populateContentFragment(fragment);
    }

    private void updateLoginState() {
        Menu menu = mNavigationView.getMenu();
        boolean unauthorised = Strs.isNullOrEmpty(getStorage().getUserToken(mMineName));
        menu.findItem(R.id.drawer_login).setVisible(unauthorised);
        menu.findItem(R.id.drawer_logout).setVisible(!unauthorised);
        updateNavigationView();
    }

    // HACK
    private void updateNavigationView() {
        try {
            Field presenterField = NavigationView.class.getDeclaredField("mPresenter");
            presenterField.setAccessible(true);
            ((NavigationMenuPresenter) presenterField.get(mNavigationView)).updateMenuView(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
