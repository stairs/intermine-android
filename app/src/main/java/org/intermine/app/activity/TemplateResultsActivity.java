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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.app.R;
import org.intermine.app.adapter.ApiPager;
import org.intermine.app.adapter.ListAdapter;
import org.intermine.app.controller.LoadOnScrollViewController;
import org.intermine.app.core.ListItems;
import org.intermine.app.core.templates.TemplateParameter;
import org.intermine.app.net.ResponseHelper;
import org.intermine.app.net.request.get.GetTemplateResultsRequest;
import org.intermine.app.util.Collections;
import org.intermine.app.util.Strs;
import org.intermine.app.util.Views;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class TemplateResultsActivity extends BaseActivity implements SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener {
    public static final int ITEMS_PER_PAGE = 15;

    public static final String TEMPLATE_NAME_KEY = "template_name_key";
    public static final String MINE_NAME_KEY = "mine_name_key";
    public static final String TEMPLATE_PARAMS_KEY = "template_params_key";
    protected LoadOnScrollViewController mViewController;
    protected boolean mLoading;

    @InjectView(R.id.list)
    ListView mListView;

    @InjectView(R.id.progress_view)
    ProgressBar mProgressBar;

    @InjectView(R.id.not_found_results_container)
    View mNotFoundView;

    private SearchView mSearchView;

    private ListAdapter mListAdapter;
    private LoadOnScrollViewController.LoadOnScrollDataController mDataController;
    private ApiPager mPager;
    private String mTemplateName;
    private ArrayList<TemplateParameter> mTemplateParams;
    private String mMineName;
    private String mQuery = Strs.EMPTY_STRING;

    // --------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------

    public static void start(Context context, String templateName, String mineName,
                             ArrayList<TemplateParameter> params) {
        Intent intent = new Intent(context, TemplateResultsActivity.class);
        intent.putExtra(TEMPLATE_NAME_KEY, templateName);
        intent.putExtra(MINE_NAME_KEY, mineName);
        intent.putParcelableArrayListExtra(TEMPLATE_PARAMS_KEY, params);
        context.startActivity(intent);
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_results_activity);
        ButterKnife.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTemplateName = getIntent().getStringExtra(TEMPLATE_NAME_KEY);
        mMineName = getIntent().getStringExtra(MINE_NAME_KEY);
        mTemplateParams = getIntent().getParcelableArrayListExtra(TEMPLATE_PARAMS_KEY);

        mListAdapter = new ListAdapter(this);
        mListView.setAdapter(mListAdapter);

        mViewController = new LoadOnScrollViewController(getDataController(), this);
        mListView.setOnScrollListener(mViewController);
        mListView.addFooterView(mViewController.getFooterView());

        if (null != mTemplateName) {
            setTitle(mTemplateName);
            setProgress(true);

            fetchTemplateResultsCount();
        }
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.templates_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_action);
        MenuItemCompat.setOnActionExpandListener(menuItem, this);
        mSearchView = (SearchView) menuItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.template_search_hint));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (null != mSearchView) {
            mSearchView.clearFocus();
        }
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        if (null != mSearchView) {
            mSearchView.clearFocus();
        }

        mListAdapter.filter(Strs.EMPTY_STRING);
        Views.setVisible(mListView);
        Views.setGone(mNotFoundView);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        mQuery = query;

        if (!Strs.isNullOrEmpty(mQuery)) {
            mListAdapter.filter(query);

            if (mListAdapter.isFilteredResultsEmpty()) {
                Views.setVisible(mNotFoundView);
                Views.setGone(mListView);
            } else {
                Views.setVisible(mListView);
                Views.setGone(mNotFoundView);
            }
        }
        return true;
    }
    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    protected LoadOnScrollViewController.LoadOnScrollDataController generateDataController() {
        return new LoadOnScrollViewController.LoadOnScrollDataController() {

            @Override
            public boolean hasMore() {
                return mPager == null || mPager.hasMorePages();
            }

            @Override
            public boolean isLoading() {
                return mLoading;
            }

            @Override
            public void loadMore() {
                mPager = mPager.next();
                fetchTemplateResults();

                mViewController.onStartLoad();
                mLoading = true;
            }
        };
    }

    protected LoadOnScrollViewController.LoadOnScrollDataController getDataController() {
        if (null == mDataController) {
            mDataController = generateDataController();
        }
        return mDataController;
    }


    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void fetchTemplateResults() {
        GetTemplateResultsRequest request = new GetTemplateResultsRequest(ListItems.class, this,
                mTemplateName, mTemplateParams, mMineName,
                mPager.getCurrentPage() * mPager.getPerPage(), ITEMS_PER_PAGE);
        execute(request, new TemplateResultsListener());
    }

    protected void fetchTemplateResultsCount() {
        GetTemplateResultsRequest request = new GetTemplateResultsRequest(Integer.class, this,
                mTemplateName, mTemplateParams, mMineName, 0, ITEMS_PER_PAGE);
        execute(request, new TemplateResultsCountListener());
    }

    protected void setProgress(boolean loading) {
        mLoading = loading;

        if (loading) {
            Views.setVisible(mProgressBar);
            Views.setGone(mListView, mNotFoundView);
        } else {
            Views.setVisible(mListView);
            Views.setGone(mProgressBar);
        }
    }

    public class TemplateResultsListener implements RequestListener<ListItems> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            setProgress(false);
            mViewController.onFinishLoad();

            Views.setVisible(mNotFoundView);
            ResponseHelper.handleSpiceException(spiceException, TemplateResultsActivity.this,
                    mMineName);
        }

        @Override
        public void onRequestSuccess(ListItems result) {
            setProgress(false);
            mViewController.onFinishLoad();

            if (null != result && !Collections.isNullOrEmpty(result.getFeatures())) {
                mListAdapter.addListItems(result);
            } else {
                Views.setVisible(mNotFoundView);
            }
        }
    }
    public class TemplateResultsCountListener implements RequestListener<Integer> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            setProgress(false);
            mViewController.onFinishLoad();

            Views.setVisible(mNotFoundView);
            ResponseHelper.handleSpiceException(spiceException, TemplateResultsActivity.this,
                    mMineName);
        }

        @Override
        public void onRequestSuccess(Integer count) {
            mPager = new ApiPager(count, 0, ITEMS_PER_PAGE);
            fetchTemplateResults();
        }
    }
}
