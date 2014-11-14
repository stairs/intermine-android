package org.intermine.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.activity.MainActivity;
import org.intermine.controller.LoadOnScrollViewController;
import org.intermine.core.GenesList;
import org.intermine.net.request.db.FavoritesCountRequest;
import org.intermine.net.request.get.GetGeneFavoritesRequest;
import org.intermine.net.request.get.GeneSearchRequest;
import org.intermine.util.Views;

public class FavoritesFragment extends GenesListFragment {
    private ApiPager mPager;

    public FavoritesFragment() {
    }

    // --------------------------------------------------------------------------------------------
    // Static methods
    // --------------------------------------------------------------------------------------------

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    private class FavoritesRequestListener implements RequestListener<GenesList> {

        @Override
        public void onRequestFailure(SpiceException e) {
            setProgress(false);
            mViewController.onFinishLoad();
            // TODO notify user?
        }

        @Override
        public void onRequestSuccess(GenesList result) {
            setProgress(false);
            mViewController.onFinishLoad();

            // first page load
            if (mPager == null)
                getGenes().clear();

            if (null != result && !result.isEmpty()) {
                getGenes().addAll(result);
                notifyDataChanged();
                Views.setVisible(mGenesListView);
                Views.setGone(mViewController.getFooterView());
            } else {
                Views.setGone(mGenesListView);
                Views.setVisible(mNotFoundView);
            }
        }
    }

    private class FavoritesCountRequestListener implements RequestListener<Integer> {

        @Override
        public void onRequestFailure(SpiceException e) {
            // TODO notify user?
        }

        @Override
        public void onRequestSuccess(Integer count) {
            mPager = new ApiPager(count, 0, GeneSearchRequest.DEFAULT_SIZE);

            fetchFavorites(0, GeneSearchRequest.DEFAULT_SIZE);
        }
    }


    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(getString(R.string.favorites));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (null == savedInstanceState) {
            fetchFavoritesCount();
        }
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    private void fetchFavoritesCount() {
        setProgress(true);

        FavoritesCountRequest request = new FavoritesCountRequest(getActivity());
        mSpiceManager.execute(request, new FavoritesCountRequestListener());
    }

    private void fetchFavorites(int from, int count) {
        GetGeneFavoritesRequest request = new GetGeneFavoritesRequest(getActivity(), from, count);
        mSpiceManager.execute(request, new FavoritesRequestListener());
    }

    @Override
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
                    fetchFavoritesCount();
                } else {
                    mPager = mPager.next();
                    fetchFavorites(mPager.getCurrentPage() * mPager.getPerPage(), mPager.getPerPage());
                }
                mViewController.onStartLoad();
                mLoading = true;
            }
        };
    }
}
