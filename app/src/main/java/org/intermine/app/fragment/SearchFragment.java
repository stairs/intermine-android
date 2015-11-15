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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.app.R;
import org.intermine.app.activity.BaseActivity;
import org.intermine.app.activity.MainActivity;
import org.intermine.app.adapter.ApiPager;
import org.intermine.app.adapter.GenesAdapter;
import org.intermine.app.controller.LoadOnScrollViewController;
import org.intermine.app.core.Gene;
import org.intermine.app.core.GenesList;
import org.intermine.app.listener.OnGeneSelectedListener;
import org.intermine.app.net.ResponseHelper;
import org.intermine.app.net.request.get.GeneSearchRequest;
import org.intermine.app.util.Sharing;
import org.intermine.app.util.Strs;
import org.intermine.app.util.Views;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import butterknife.InjectView;
import butterknife.OnItemClick;

public class SearchFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    private static final String QUERY_KEY = "query_key";
    private static final String EXPAND_SEARCH_VIEW_KEY = "expand_search_view_key";

    @InjectView(R.id.genes)
    protected ListView mGenesListView;

    @InjectView(R.id.not_found_results_container)
    protected View mNotFoundView;

    @InjectView(R.id.info_container)
    protected View mInfoContainer;

    @InjectView(R.id.progress_view)
    protected ProgressBar mProgressView;

    @InjectView(R.id.progress_bar)
    protected ProgressBar mProgressBar;
    protected boolean mLoading;
    protected LoadOnScrollViewController mViewController;
    private SearchView mSearchView;
    private ApiPager mPager;
    private LoadOnScrollViewController.LoadOnScrollDataController mDataController;
    private OnSearchRequestsFinishedAsyncTask mAsyncTask;

    private GenesAdapter mGenesAdapter;
    private List<Gene> mGenes;

    private Map<String, Integer> mMine2ResultsCount = new HashMap<>();

    private CountDownLatch mCountDownLatch;

    private OnGeneSelectedListener mListener;

    private String mGeneFavoritesListName;
    private String mQuery = Strs.EMPTY_STRING;
    private boolean mExpandSearchViewOnStartup;

    private int mStatusBarColor;

    private MultiChoiceModeListener mMultiListener = new MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int pos, long id, boolean checked) {
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.gene_view_menu, menu);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mStatusBarColor = getActivity().getWindow().getStatusBarColor();
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.dark_gray));
            }
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.favourites:
                    addGenesToFavorites();
                    Toast.makeText(getActivity(), R.string.genes_added_to_favorites, Toast.LENGTH_LONG).show();
                    mode.finish();
                    return true;
                case R.id.share:
                    Intent intent = Sharing.generateIntentToSendText(getSelectedGenes());
                    startActivity(Intent.createChooser(intent,
                            getResources().getString(R.string.share_message)));
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(mStatusBarColor);
            }
        }
    };

    public static SearchFragment newInstance(boolean expandSearchView) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXPAND_SEARCH_VIEW_KEY, expandSearchView);
        fragment.setArguments(bundle);
        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGenesListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        mGenesListView.setMultiChoiceModeListener(mMultiListener);

        mGenes = new ArrayList<>();
        mGenesAdapter = new GenesAdapter(getActivity());
        mGenesAdapter.updateGenes(mGenes);
        mGenesListView.setAdapter(mGenesAdapter);

        mViewController = new LoadOnScrollViewController(getDataController(), getActivity());
        mGenesListView.setOnScrollListener(mViewController);
        mGenesListView.addFooterView(mViewController.getFooterView());

        if (null != savedInstanceState) {
            mQuery = savedInstanceState.getString(QUERY_KEY);

            if (!Strs.isNullOrEmpty(mQuery)) {
                onQueryTextSubmit(mQuery);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mGeneFavoritesListName = getString(R.string.gene_favorites_list_name);

        Bundle bundle = getArguments();

        if (null != bundle) {
            mExpandSearchViewOnStartup = bundle.getBoolean(EXPAND_SEARCH_VIEW_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnGeneSelectedListener) context;

        ((MainActivity) context).onSectionAttached(getString(R.string.search));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (!Strs.isNullOrEmpty(mQuery)) {
            outState.putString(QUERY_KEY, mQuery);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gene_search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_action);

        mSearchView = (SearchView) item.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.gene_search_hint));

        final EditText searchTextView = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, 0);
        } catch (Exception e) {
        }

        if (mExpandSearchViewOnStartup) {
            item.expandActionView();
        }
        super.onCreateOptionsMenu(menu, inflater);
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
        mQuery = query;

        if (!Strs.isNullOrEmpty(mQuery)) {
            if (null != mSearchView) {
                mSearchView.clearFocus();
            }

            mPager = null;
            setProgress(true);
            performSearchRequests(mQuery, GeneSearchRequest.JSON_FORMAT, 0);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @OnItemClick(R.id.genes)
    public void onGeneSelected(int position) {
        if (null != mListener) {
            Gene gene = (Gene) mGenesAdapter.getItem(position);
            mListener.onGeneSelected(gene);
        }
    }

    protected void setProgress(boolean loading) {
        mLoading = loading;
        // TODO: fix
        Views.setGone(mInfoContainer);

        if (loading) {
            Views.setVisible(mProgressView);
            Views.setGone(mGenesListView, mNotFoundView);
        } else {
            Views.setVisible(mGenesListView);
            Views.setGone(mProgressView);
        }
    }

    protected List<Gene> getSelectedGenes() {
        List<Gene> genes = org.intermine.app.util.Collections.newArrayList();
        SparseBooleanArray checkedItemIds = mGenesListView.getCheckedItemPositions();

        for (int i = 0; i < mGenesAdapter.getCount(); i++) {
            if (checkedItemIds.get(i)) {
                Gene gene = (Gene) mGenesAdapter.getItem(i);
                genes.add(gene);
            }
        }
        return genes;
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void addGenesToFavorites() {
        SparseBooleanArray checkedItemIds = mGenesListView.getCheckedItemPositions();
        for (int i = 0; i < mGenesAdapter.getCount(); i++) {
            if (checkedItemIds.get(i)) {
                Gene gene = (Gene) mGenesAdapter.getItem(i);
                getStorage().addGeneToFavorites(gene);
            }
        }
    }

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
                if (mPager == null) {
                    performSearchRequests(mQuery, GeneSearchRequest.JSON_FORMAT, 0);
                } else {
                    mPager = mPager.next();
                    performSearchRequests(mQuery, GeneSearchRequest.JSON_FORMAT,
                            mPager.getCurrentPage() * mPager.getPerPage());
                }
                mViewController.onStartLoad();
                mLoading = true;
            }
        };
    }

    private void performSearchRequests(String query, String format, int start) {
        Set<String> selectedMines = getStorage().getSelectedMineNames();

        if (null != mAsyncTask && !mAsyncTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            mAsyncTask.cancel(true);
        }

        mCountDownLatch = new CountDownLatch(selectedMines.size());

        mAsyncTask = new OnSearchRequestsFinishedAsyncTask();
        mAsyncTask.execute();

        for (String mine : selectedMines) {
            Integer count = mMine2ResultsCount.get(mine);

            if (0 == start || (null != count &&
                    (mPager.getCurrentPage() + 1) * mPager.getPerPage() < count)) {
                GeneSearchRequest request = new GeneSearchRequest(getActivity(),
                        query, mine, format, start);
                execute(request, new GeneSearchRequestListener());
            } else {
                mCountDownLatch.countDown();
            }
        }
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------
    private class GeneSearchRequestListener implements RequestListener<GenesList> {

        @Override
        public void onRequestFailure(SpiceException e) {
            mCountDownLatch.countDown();
            ResponseHelper.handleSpiceException(e, (BaseActivity) getActivity(), null);
        }

        @Override
        public void onRequestSuccess(GenesList result) {
            mCountDownLatch.countDown();

            // first page load
            if (null == mPager) {
                mGenes.clear();
                mPager = new ApiPager(0, 0, GeneSearchRequest.DEFAULT_SIZE);
                mMine2ResultsCount = new HashMap<>();
            }

            if (0 == mPager.getCurrentPage()) {
                mPager = new ApiPager(mPager.getTotal() + result.getResultsCount(), 0, GeneSearchRequest.DEFAULT_SIZE);
            }

            if (null != result && !result.isEmpty()) {
                mGenes.addAll(result);
                Collections.sort(mGenes);
                mGenesAdapter.notifyDataSetChanged();
                mMine2ResultsCount.put(result.get(0).getMine(), result.getResultsCount());
            }

            if (!mGenes.isEmpty()) {
                setProgress(false);
            }
        }
    }

    private class OnSearchRequestsFinishedAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            if (!mGenes.isEmpty()) {
                Views.setVisible(mProgressBar);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (0 < mCountDownLatch.getCount()) {
                try {
                    mCountDownLatch.await();
                } catch (InterruptedException e) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mViewController.onFinishLoad();
            Views.setGone(mProgressBar);

            if (mGenes.isEmpty()) {
                Views.setGone(mGenesListView);
                Views.setVisible(mNotFoundView);
            } else {
                Views.setVisible(mGenesListView);
                Views.setGone(mNotFoundView);
            }

            setProgress(false);
        }

        @Override
        protected void onCancelled() {
            Views.setGone(mProgressBar);
        }
    }
}