package org.intermine.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.activity.MainActivity;
import org.intermine.adapter.GenesAdapter;
import org.intermine.controller.LoadOnScrollViewController;
import org.intermine.core.Gene;
import org.intermine.core.GenesList;
import org.intermine.net.request.db.AddGenesToFavoritesRequest;
import org.intermine.net.request.get.GeneSearchRequest;
import org.intermine.storage.Storage;
import org.intermine.util.Emails;
import org.intermine.util.Mines;
import org.intermine.util.Strs;
import org.intermine.util.Views;
import org.intermine.view.ProgressView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    private static final String QUERY_KEY = "query_key";

    @InjectView(R.id.genes)
    protected ListView mGenesListView;

    @InjectView(R.id.not_found_results_container)
    protected View mNotFoundView;

    @InjectView(R.id.info_container)
    protected View mInfoContainer;

    @InjectView(R.id.progress_view)
    protected ProgressView mProgressView;

    @InjectView(R.id.progress_bar)
    protected ProgressBar mProgressBar;

    private SearchView mSearchView;


    protected boolean mLoading;
    protected LoadOnScrollViewController mViewController;
    private String mQuery = "";
    private GenesListFragment.OnGeneSelectedListener mOnGeneSelectedListener;
    private LoadOnScrollViewController.LoadOnScrollDataController mDataController;
    private GenesAdapter mGenesAdapter;
    private List<Gene> mGenes;

    private Map<String, Integer> mMine2ResultsCount = new HashMap<String, Integer>();

    private CountDownLatch mCountDownLatch;

    private ApiPager mPager;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------


    private class GeneSearchRequestListener implements RequestListener<GenesList> {

        @Override
        public void onRequestFailure(SpiceException e) {
            mCountDownLatch.countDown();
            // TODO notify user?
        }

        @Override
        public void onRequestSuccess(GenesList result) {
            mCountDownLatch.countDown();

            // first page load
            if (null == mPager) {
                mGenes.clear();
                mPager = new ApiPager(0, 0, GeneSearchRequest.DEFAULT_SIZE);
                mMine2ResultsCount = new HashMap<String, Integer>();
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
                Views.setVisible(mProgressBar);
                setProgress(false);
            }
        }
    }

    private class OnSearchRequestsFinishedAsyncTask extends AsyncTask<Void, Void, Void> {

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
    }
    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(getActivity(), view);

        mGenesListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        mGenesListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                List<Gene> selectedGenes = getSelectedGenes();

                switch (item.getItemId()) {
                    case R.id.favourites:
                        //TODO: refactore
                        executeRequest(new AddGenesToFavoritesRequest(
                                getActivity(), selectedGenes), null);
                        Toast.makeText(getActivity(),
                                R.string.genes_added_to_favorites, Toast.LENGTH_LONG).show();
                        mode.finish();
                        return true;
                    case R.id.email:
                        Intent intent = Emails.generateIntentToSendEmails(selectedGenes);
                        startActivity(intent);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        mGenes = new ArrayList<>();
        mGenesAdapter = new GenesAdapter(getActivity());
        mGenesAdapter.updateGenes(mGenes);
        mGenesListView.setAdapter(mGenesAdapter);

        mViewController = new LoadOnScrollViewController(getDataController(), getActivity());
        mGenesListView.setOnScrollListener(mViewController);
        mGenesListView.addFooterView(mViewController.getFooterView());

        mGenesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != mOnGeneSelectedListener) {
                    Gene gene = (Gene) parent.getItemAtPosition(position);
                    mOnGeneSelectedListener.onGeneSelected(gene);
                }
            }
        });

        if (null != savedInstanceState) {
            mQuery = savedInstanceState.getString(QUERY_KEY);

            if (!Strs.isNullOrEmpty(mQuery)) {
                onQueryTextSubmit(mQuery);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnGeneSelectedListener = (GenesListFragment.OnGeneSelectedListener) activity;

        ((MainActivity) activity).onSectionAttached(getString(R.string.search));
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
        if (!((MainActivity) getActivity()).getNavigationDrawer().isDrawerOpen()) {
            inflater.inflate(R.menu.gene_search_menu, menu);

            final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            final SearchableInfo info = searchManager.getSearchableInfo(getActivity().getComponentName());

            final MenuItem searchItem = menu.findItem(R.id.search_action);
            mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            mSearchView.setSearchableInfo(info);
            mSearchView.setOnQueryTextListener(this);
            super.onCreateOptionsMenu(menu, inflater);
        }
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

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
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
        List<Gene> genes = new ArrayList<Gene>();

        SparseBooleanArray checkedItemIds = mGenesListView.getCheckedItemPositions();
        for (int i = 0; i < mGenesAdapter.getCount(); i++) {
            if (checkedItemIds.get(i)) {
                genes.add((Gene) mGenesAdapter.getItem(i));
            }
        }
        return genes;
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
        Set<String> selectedMines = getStorage().getMineNames();

        mCountDownLatch = new CountDownLatch(selectedMines.size());
        new OnSearchRequestsFinishedAsyncTask().execute();

        for (String mine : selectedMines) {
            Integer count = mMine2ResultsCount.get(mine);

            if (0 == start || (null != count &&
                    (mPager.getCurrentPage() + 1) * mPager.getPerPage() < count)) {
                GeneSearchRequest request = new GeneSearchRequest(getActivity(),
                        Mines.getMineBaseUrl(getActivity(), mine), query, mine, format, start);
                executeRequest(request, new GeneSearchRequestListener());
            } else {
                mCountDownLatch.countDown();
            }
        }
    }
}