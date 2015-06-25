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
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.app.R;
import org.intermine.app.activity.BaseActivity;
import org.intermine.app.activity.MainActivity;
import org.intermine.app.adapter.TemplatesAdapter;
import org.intermine.app.core.templates.Template;
import org.intermine.app.net.ResponseHelper;
import org.intermine.app.net.request.get.GetTemplatesRequest;
import org.intermine.app.net.request.get.GetTemplatesRequest.Templates;
import org.intermine.app.util.Collections;
import org.intermine.app.util.Strs;
import org.intermine.app.util.Views;

import butterknife.InjectView;
import butterknife.OnItemClick;

public class TemplatesFragment extends BaseFragment implements SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener {
    public static final String MINE_NAME_KEY = "mine_name";

    public static final long TEMPLATES_CACHE_EXPIRY_DURATION = 1000 * 60 * 10;

    @InjectView(R.id.templates)
    ListView mTemplates;

    @InjectView(R.id.not_found_results_container)
    View mNotFoundView;

    @InjectView(R.id.progress_view)
    ProgressBar mProgressView;

    private SearchView mSearchView;

    private TemplatesAdapter mTemplatesAdapter;

    private OnTemplateSelectedListener mOnTemplateSelectedListener;

    private String mMineName;
    private String mQuery = Strs.EMPTY_STRING;

    // --------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------

    public static TemplatesFragment newInstance(String mineName) {
        TemplatesFragment fragment = new TemplatesFragment();

        Bundle bundle = new Bundle();
        bundle.putString(MINE_NAME_KEY, mineName);
        fragment.setArguments(bundle);
        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.templates_fragment, container, false);
    }
    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTemplatesAdapter = new TemplatesAdapter(getActivity());
        mTemplates.setAdapter(mTemplatesAdapter);

        setProgress(true);
        fetchTemplates();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnTemplateSelectedListener = (OnTemplateSelectedListener) activity;

        Bundle bundle = getArguments();

        if (null != bundle) {
            mMineName = bundle.getString(MINE_NAME_KEY);
        }

        String title = mMineName + " " + getString(R.string.templates);
        ((MainActivity) activity).onSectionAttached(title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.templates_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_action);
        MenuItemCompat.setOnActionExpandListener(menuItem, this);
        mSearchView = (SearchView) menuItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.template_search_hint));
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        mSearchView = null;
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

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

        mTemplatesAdapter.filter(Strs.EMPTY_STRING);
        Views.setVisible(mTemplates);
        Views.setGone(mNotFoundView);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        mQuery = query;

        if (!Strs.isNullOrEmpty(mQuery)) {
            mTemplatesAdapter.filter(query);

            if (Collections.isNullOrEmpty(mTemplatesAdapter.getFilteredTemplates())) {
                Views.setVisible(mNotFoundView);
                Views.setGone(mTemplates);
            } else {
                Views.setVisible(mTemplates);
                Views.setGone(mNotFoundView);
            }
        }
        return true;
    }

    @OnItemClick(R.id.templates)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mOnTemplateSelectedListener) {
            Template selected = (Template) mTemplatesAdapter.getItem(position);
            mOnTemplateSelectedListener.onTemplateSelected(selected, mMineName);
        }
    }

    protected void fetchTemplates() {
        GetTemplatesRequest request = new GetTemplatesRequest(getActivity(), mMineName);
        execute(request, mMineName, TEMPLATES_CACHE_EXPIRY_DURATION, new GetTemplatesListener());
    }

    protected void setProgress(boolean loading) {
        if (loading) {
            Views.setVisible(mProgressView);
            Views.setGone(mTemplates, mNotFoundView);
        } else {
            Views.setVisible(mTemplates);
            Views.setGone(mProgressView);
        }
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    public static interface OnTemplateSelectedListener {
        void onTemplateSelected(Template template, String mineName);
    }
    public class GetTemplatesListener implements RequestListener<Templates> {

        @Override
        public void onRequestFailure(SpiceException ex) {
            setProgress(false);
            ResponseHelper.handleSpiceException(ex, (BaseActivity) getActivity(), mMineName);
        }

        @Override
        public void onRequestSuccess(Templates result) {
            setProgress(false);

            if (result == null || result.isEmpty()) {
                Views.setVisible(mNotFoundView);
                Views.setGone(mTemplates);
            } else {
                Views.setVisible(mTemplates);
                Views.setGone(mNotFoundView);

                mTemplatesAdapter.updateData(result.values());
            }
        }
    }
}