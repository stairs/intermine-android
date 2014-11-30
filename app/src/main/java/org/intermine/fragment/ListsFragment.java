package org.intermine.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.activity.BaseActivity;
import org.intermine.activity.MainActivity;
import org.intermine.adapter.DividerItemDecoration;
import org.intermine.adapter.ListsAdapter;
import org.intermine.core.List;
import org.intermine.net.ResponseHelper;
import org.intermine.net.request.get.GetListsRequest;
import org.intermine.util.Views;
import org.intermine.view.ProgressView;

import butterknife.InjectView;

public class ListsFragment extends BaseFragment implements ListsAdapter.OnItemClickListener {
    public static final String TAG = ListsFragment.class.getSimpleName();
    public static final String MINE_NAME_KEY = "mine_name_key";

    public static final long LISTS_CACHE_EXPIRY_DURATION = 1000 * 60 * 10;

    @InjectView(R.id.progress_view)
    ProgressView mProgressView;

    @InjectView(R.id.lists)
    RecyclerView mRecyclerView;

    private String mMineName;

    private OnListSelectedListener mListener;

    // --------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------

    public static ListsFragment newInstance(String mineName) {
        ListsFragment fragment = new ListsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MINE_NAME_KEY, mineName);
        fragment.setArguments(bundle);
        return fragment;
    }
    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    public static interface OnListSelectedListener {
        void onListSelected(org.intermine.core.List list, String mineName);
    }

    class GetListsRequestListener implements RequestListener<GetListsRequest.Lists> {

        @Override
        public void onRequestFailure(SpiceException ex) {
            setProgress(false);
            ResponseHelper.handleSpiceException(ex, (BaseActivity) getActivity(), mMineName);
        }

        @Override
        public void onRequestSuccess(GetListsRequest.Lists lists) {
            setProgress(false);
            mRecyclerView.setAdapter(new ListsAdapter(lists, ListsFragment.this));
        }
    }

    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.lists_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL));

        setProgress(true);
        fetchLists();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnListSelectedListener) activity;

        Bundle bundle = getArguments();

        if (null != bundle) {
            mMineName = bundle.getString(MINE_NAME_KEY);
        }

        String title = mMineName + " " + getString(R.string.lists);
        ((MainActivity) activity).onSectionAttached(title);
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    @Override
    public void onItemClick(List list) {
        mListener.onListSelected(list, mMineName);
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void fetchLists() {
        GetListsRequest request = new GetListsRequest(getActivity(), mMineName, null);
        execute(request, mMineName, LISTS_CACHE_EXPIRY_DURATION, new GetListsRequestListener());
    }

    protected void setProgress(boolean loading) {
        if (loading) {
            Views.setVisible(mProgressView);
            Views.setGone(mRecyclerView);
        } else {
            Views.setVisible(mRecyclerView);
            Views.setGone(mProgressView);
        }
    }

    public void setMineName(String mineName) {
        mMineName = mineName;
    }
}