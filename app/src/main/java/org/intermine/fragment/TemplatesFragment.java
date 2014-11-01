package org.intermine.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.activity.MainActivity;
import org.intermine.adapter.GenesAdapter;
import org.intermine.controller.LoadOnScrollViewController;
import org.intermine.core.Gene;
import org.intermine.net.request.db.AddGenesToFavoritesRequest;
import org.intermine.net.request.get.GeneSearchRequest;
import org.intermine.net.request.post.PostListResultsRequest;
import org.intermine.util.Emails;
import org.intermine.util.Views;
import org.intermine.view.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class TemplatesFragment extends BaseFragment {
    protected ListView mGenesListView;
    protected View mNotFoundView;
    protected boolean mLoading;
    protected LoadOnScrollViewController mViewController;
    private GenesListFragment.OnGeneSelectedListener mOnGeneSelectedListener;
    private ProgressView mProgressView;
    private LoadOnScrollViewController.LoadOnScrollDataController mDataController;
    private GenesAdapter mGenesAdapter;
    private List<List> mGenes;

    private ApiPager mPager;

    public TemplatesFragment() {
    }

    public static TemplatesFragment newInstance() {
        TemplatesFragment fragment = new TemplatesFragment();
        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lists_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressView = (ProgressView) view.findViewById(R.id.progress_view);
        mNotFoundView = view.findViewById(R.id.not_found_results_container);

        mGenesListView = (ListView) view.findViewById(R.id.genes);
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

        mGenes = new ArrayList<List>();
        mGenesAdapter = new GenesAdapter(getActivity());
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

        setProgress(true);
        performGetListResultsRequest("bla");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnGeneSelectedListener = (GenesListFragment.OnGeneSelectedListener) activity;

        ((MainActivity) activity).onSectionAttached(getString(R.string.lists));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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


    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------


    // --------------------------------------------------------------------------------------------
    // Helper Methods
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
                if (mPager == null) {
                    performGetListResultsRequest("bla");
                } else {
                    mPager = mPager.next();
                    performGetListResultsRequest("bla");
//                    performGetListResultsRequest("bla", GeneSearchRequest.JSON_FORMAT,
//                            mPager.getCurrentPage() * mPager.getPerPage());
                }
                mViewController.onStartLoad();
                mLoading = true;
            }
        };
    }

    protected void performGetListResultsRequest(String listName) {
        PostListResultsRequest request = new PostListResultsRequest(getActivity(), listName);
        executeRequest(request, new ListResultsListener());
    }

    protected void setProgress(boolean loading) {
        mLoading = loading;

        if (loading) {
            Views.setVisible(mProgressView);
            Views.setGone(mGenesListView, mNotFoundView);
        } else {
            Views.setVisible(mGenesListView);
            Views.setGone(mProgressView);
        }
    }

    public class ListResultsListener implements RequestListener<List> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            setProgress(false);
            Toast.makeText(getActivity(), spiceException.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(List result) {
            // first page load
            if (null == mPager) {
                mGenes.clear();
                mPager = new ApiPager(0, 0, GeneSearchRequest.DEFAULT_SIZE);
            }

            if (0 == mPager.getCurrentPage()) {
                mPager = new ApiPager(mPager.getTotal() + result.size(), 0, GeneSearchRequest.DEFAULT_SIZE);
            }

            if (null != result && !result.isEmpty()) {
                mGenesAdapter.updateGenes(result);
            }

            setProgress(false);
        }
    }
}
