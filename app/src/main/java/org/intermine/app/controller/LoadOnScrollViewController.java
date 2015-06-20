package org.intermine.app.controller;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import org.intermine.app.util.Views;

public class LoadOnScrollViewController implements AbsListView.OnScrollListener {

    private AbsListView.OnScrollListener mWrapped;
    private LoadOnScrollDataController mDataController;
    private LayoutInflater mLayoutInflater;
    private View mFooterView = null;
    private Context mContext;

    int mCurrentFirstVisibleItem = 0;
    int mCurrentVisibleItemCount = 0;
    int mCurrentTotalItemCount = 0;
    int mCurrentScrollState = -1;

    public LoadOnScrollViewController(LoadOnScrollDataController dataController, Context ctx) {
        mDataController = dataController;
        mContext = ctx;
    }

    public View getFooterView() {
        if (null == mFooterView) {
            mFooterView = new ProgressBar(mContext);
        }
        return mFooterView;
    }

    public void onStartLoad() {
        Views.setVisible(getFooterView());
    }

    public void onFinishLoad() {
        Views.setGone(getFooterView());
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mCurrentScrollState = scrollState;
        loadIfScrollComplete();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mCurrentFirstVisibleItem = firstVisibleItem;
        mCurrentVisibleItemCount = visibleItemCount;
        mCurrentTotalItemCount = totalItemCount;
    }

    private void loadIfScrollComplete() {
        if (mCurrentVisibleItemCount > 0
                && mCurrentScrollState == SCROLL_STATE_IDLE
                && (mCurrentFirstVisibleItem + mCurrentVisibleItemCount == mCurrentTotalItemCount)) {
            if (mDataController.hasMore() && !mDataController.isLoading()) {
                mDataController.loadMore();
            }
        }
    }

    public interface LoadOnScrollDataController {
        boolean hasMore();

        boolean isLoading();

        void loadMore();
    }
}
