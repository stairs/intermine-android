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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.app.R;
import org.intermine.app.activity.BaseActivity;
import org.intermine.app.activity.MainActivity;
import org.intermine.app.adapter.ApiPager;
import org.intermine.app.adapter.ListAdapter;
import org.intermine.app.controller.LoadOnScrollViewController;
import org.intermine.app.core.ListItems;
import org.intermine.app.listener.OnGeneSelectedListener;
import org.intermine.app.net.ResponseHelper;
import org.intermine.app.net.request.post.FetchListResultsRequest;
import org.intermine.app.util.Collections;
import org.intermine.app.util.Strs;
import org.intermine.app.util.Views;

import butterknife.InjectView;

public class GenesListFragment extends BaseFragment {
    public static final String LIST_KEY = "list_key";
    public static final String MINE_NAME_KEY = "mine_name_key";
    public static final int ITEMS_PER_PAGE = 15;

    @InjectView(R.id.list)
    protected ListView mListView;

    @InjectView(R.id.not_found_results_container)
    protected View mNotFoundView;

    @InjectView(R.id.auth_required_label)
    protected View mAuthRequiredView;

    @InjectView(R.id.progress_view)
    protected ProgressBar mProgressView;

    private ListAdapter mListAdapter;

    protected LoadOnScrollViewController mViewController;
    private LoadOnScrollViewController.LoadOnScrollDataController mDataController;
    private ApiPager mPager;

    private OnGeneSelectedListener mListener;

    private org.intermine.app.core.List mList;
    private String mMineName;

    protected boolean mLoading;

    public static GenesListFragment newInstance(org.intermine.app.core.List list, String mineName) {
        GenesListFragment fragment = new GenesListFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(LIST_KEY, list);
        bundle.putString(MINE_NAME_KEY, mineName);
        fragment.setArguments(bundle);
        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    private class ListResultsListener implements RequestListener<ListItems> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            setProgress(false);
            mViewController.onFinishLoad();

            Views.setVisible(mNotFoundView);
            Views.setGone(mListView);
            ResponseHelper.handleSpiceException(spiceException, (BaseActivity) getActivity(),
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
    // --------------------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.genes_list_fragment, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnGeneSelectedListener) activity;

        if (activity instanceof MainActivity) {
            ((MainActivity) activity).onSectionAttached(getString(R.string.favorites));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        if (null != bundle) {
            mMineName = bundle.getString(MINE_NAME_KEY);
            mList = bundle.getParcelable(LIST_KEY);
        }

        mListAdapter = new ListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

        mViewController = new LoadOnScrollViewController(getDataController(), getActivity());
        mListView.setOnScrollListener(mViewController);
        mListView.addFooterView(mViewController.getFooterView());

        if (null != mList && !Strs.isNullOrEmpty(mMineName)) {
            setProgress(true);

            if (null == mPager) {
                mPager = new ApiPager(mList.getSize(), 0, ITEMS_PER_PAGE);
            }
            performGetListResultsRequest();
        } else {
            setProgress(false);
            mViewController.onFinishLoad();
            Views.setVisible(mNotFoundView);

            if (Strs.isNullOrEmpty(mMineName)) {
                Views.setVisible(mAuthRequiredView);
            }
        }
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected LoadOnScrollViewController.LoadOnScrollDataController getDataController() {
        if (null == mDataController) {
            mDataController = generateDataController();
        }
        return mDataController;
    }

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
                performGetListResultsRequest();

                mViewController.onStartLoad();
                mLoading = true;
            }
        };
    }

    protected void performGetListResultsRequest() {
        FetchListResultsRequest request = new FetchListResultsRequest(getActivity(), mMineName,
                mList.getName(), mPager.getCurrentPage() * mPager.getPerPage(), mPager.getPerPage());
        execute(request, new ListResultsListener());
    }

    protected void setProgress(boolean loading) {
        mLoading = loading;

        if (loading) {
            Views.setVisible(mProgressView);
            Views.setGone(mListView, mNotFoundView);
        } else {
            Views.setVisible(mListView);
            Views.setGone(mProgressView);
        }
    }
}