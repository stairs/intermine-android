package org.intermine.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.activity.MainActivity;
import org.intermine.adapter.GenesAdapter;
import org.intermine.adapter.ListAdapter;
import org.intermine.controller.LoadOnScrollViewController;
import org.intermine.core.Gene;
import org.intermine.core.ListItems;
import org.intermine.net.request.post.PostListResultsRequest;
import org.intermine.util.Collections;
import org.intermine.util.Views;
import org.intermine.view.ProgressView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class GenesListFragment extends BaseFragment {
    public static final int ITEMS_PER_PAGE = 15;

    @InjectView(R.id.list)
    protected ListView mListView;

    @InjectView(R.id.not_found_results_container)
    protected View mNotFoundView;

    @InjectView(R.id.info_container)
    protected View mInfoContainer;

    @InjectView(R.id.progress_view)
    protected ProgressView mProgressView;

    @InjectView(R.id.progress_bar)
    protected ProgressBar mProgressBar;

    private ListAdapter mListAdapter;

    protected LoadOnScrollViewController mViewController;
    private LoadOnScrollViewController.LoadOnScrollDataController mDataController;
    private ApiPager mPager;

    private OnGeneSelectedListener mListener;

    private org.intermine.core.List mList;

    protected boolean mLoading;

    public static GenesListFragment newInstance(org.intermine.core.List list) {
        GenesListFragment fragment = new GenesListFragment();
        fragment.setList(list);
        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    public interface OnGeneSelectedListener {
        void onGeneSelected(Gene gene);
    }

    public class ListResultsListener implements RequestListener<ListItems> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            setProgress(false);
            mViewController.onFinishLoad();

            Toast.makeText(getActivity(), spiceException.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(ListItems result) {
            setProgress(false);
            mViewController.onFinishLoad();

            if (null != result && !Collections.isNullOrEmpty(result.getFeatures())) {
                mListAdapter.updateData(result);
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
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListAdapter = new ListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

        mViewController = new LoadOnScrollViewController(getDataController(), getActivity());
        mListView.setOnScrollListener(mViewController);
        mListView.addFooterView(mViewController.getFooterView());

        if (null != mList) {
            setProgress(true);

            if (null == mPager) {
                mPager = new ApiPager(mList.getSize(), 0, ITEMS_PER_PAGE);
            }
            performGetListResultsRequest();
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
        PostListResultsRequest request = new PostListResultsRequest(getActivity(), mList.getName(),
                mPager.getCurrentPage() * mPager.getPerPage(), mPager.getPerPage());
        executeRequest(request, new ListResultsListener());
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

    public void setList(org.intermine.core.List list) {
        mList = list;
    }
}
