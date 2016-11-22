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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.intermine.app.R;
import org.intermine.app.activity.MainActivity;
import org.intermine.app.adapter.GenesAdapter;
import org.intermine.app.core.Gene;
import org.intermine.app.listener.OnGeneSelectedListener;
import org.intermine.app.util.Collections;
import org.intermine.app.util.Views;

import java.util.List;

import butterknife.BindView;

public class FavoritesListFragment extends BaseFragment {
    @BindView(R.id.list)
    protected ListView mListView;

    @BindView(R.id.not_found_results_container)
    protected View mNotFoundView;

    @BindView(R.id.progress_view)
    protected ProgressBar mProgressView;

    private GenesAdapter mGenesAdapter;
    private OnGeneSelectedListener mListener;
    private GeneFavoritesAsyncTask mAsyncTask;

    protected boolean mLoading;

    public static FavoritesListFragment newInstance() {
        FavoritesListFragment fragment = new FavoritesListFragment();
        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    private class GeneFavoritesAsyncTask extends AsyncTask<Void, Void, List<Gene>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(true);
        }

        @Override
        protected List<Gene> doInBackground(Void... params) {
            List<Gene> genes = getStorage().getGeneFavorites();
            return genes;
        }

        @Override
        protected void onPostExecute(List<Gene> genes) {
            super.onPostExecute(genes);

            if (Collections.isNullOrEmpty(genes)) {
                Views.setVisible(mNotFoundView);
                Views.setGone(mListView, mProgressView);
            } else {
                mGenesAdapter.updateGenes(genes);
                setProgress(false);
            }
        }
    }

    // --------------------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorites_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnGeneSelectedListener) context;

        if (context instanceof MainActivity) {
            ((MainActivity) context).onSectionAttached(getString(R.string.favorites));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGenesAdapter = new GenesAdapter(getContext());
        mListView.setAdapter(mGenesAdapter);

        mAsyncTask = new GeneFavoritesAsyncTask();
        mAsyncTask.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAsyncTask.cancel(true);
        mAsyncTask = null;
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void setProgress(boolean loading) {
        mLoading = loading;

        if (loading) {
            Views.setVisible(mProgressView);
            Views.setGone(mListView, mNotFoundView);
        } else {
            Views.setVisible(mListView);
            Views.setGone(mProgressView, mNotFoundView);
        }
    }
}
