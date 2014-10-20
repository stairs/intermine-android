package org.intermine.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.intermine.R;
import org.intermine.activity.MainActivity;
import org.intermine.adapter.GenesAdapter;
import org.intermine.controller.LoadOnScrollViewController;
import org.intermine.core.Gene;
import org.intermine.util.Views;
import org.intermine.view.ProgressView;

import java.util.ArrayList;
import java.util.List;

public abstract class GenesListFragment extends BaseFragment {
    protected ListView mGenesListView;
    protected View mNotFoundView;
    protected boolean mLoading;
    protected LoadOnScrollViewController mViewController;
    private OnGeneSelectedListener mOnGeneSelectedListener;
    private ProgressView mProgressView;
    private LoadOnScrollViewController.LoadOnScrollDataController mDataController;
    private GenesAdapter mGenesAdapter;
    private List<Gene> mGenes;

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mOnGeneSelectedListener = (OnGeneSelectedListener) activity;

        ((MainActivity) activity).onSectionAttached(getString(R.string.search));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (null == savedInstanceState) {
            mGenesListView = (ListView) view.findViewById(R.id.genes);
            mProgressView = (ProgressView) view.findViewById(R.id.progress_view);
            mNotFoundView = view.findViewById(R.id.not_found_results_container);

            mGenes = new ArrayList<Gene>();
            mGenesAdapter = new GenesAdapter(getActivity());
            mGenesAdapter.updateGenes(mGenes);
            mGenesListView.setAdapter(mGenesAdapter);

            mViewController = new LoadOnScrollViewController(getDataController(),
                    GenesListFragment.this.getActivity());
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
        }
    }

    protected void setProgress(boolean loading) {
        mLoading = loading;

        if (loading) {
            Views.setVisible(mProgressView);
            Views.setGone(mGenesListView);
        } else {
            Views.setVisible(mGenesListView);
            Views.setGone(mProgressView);
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

    protected void notifyDataChanged() {
        mGenesAdapter.notifyDataSetChanged();
    }

    public List<Gene> getGenes() {
        return mGenes;
    }

    protected abstract LoadOnScrollViewController.LoadOnScrollDataController generateDataController();

    public static interface OnGeneSelectedListener {
        void onGeneSelected(Gene gene);
    }
}
