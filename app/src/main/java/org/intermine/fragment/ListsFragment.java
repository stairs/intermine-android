package org.intermine.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.activity.BaseActivity;
import org.intermine.activity.MainActivity;
import org.intermine.adapter.ListsAdapter;
import org.intermine.core.List;
import org.intermine.net.ResponseHelper;
import org.intermine.net.request.get.GetListsRequest;
import org.intermine.util.Views;
import org.intermine.view.ProgressView;

import butterknife.InjectView;
import butterknife.OnItemClick;

public class ListsFragment extends BaseFragment {
    public static final String TAG = ListsFragment.class.getSimpleName();

    @InjectView(R.id.progress_view)
    ProgressView mProgressView;

    @InjectView(R.id.lists)
    ListView mListsView;

    private ListsAdapter mAdapter;

    private String mMineName;

    private OnListSelectedListener mListener;

    // --------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------

    public static ListsFragment newInstance(String mineName) {
        ListsFragment fragment = new ListsFragment();
        fragment.setMineName(mineName);
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
            mAdapter.setLists(lists);
            mAdapter.notifyDataSetChanged();
        }
    }

    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.lists_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new ListsAdapter(getActivity());
        mListsView.setAdapter(mAdapter);

        setProgress(true);
        fetchLists();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnListSelectedListener) activity;

        ((MainActivity) activity).onSectionAttached(getString(R.string.lists));
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    @OnItemClick(R.id.lists)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List list = (List) mAdapter.getItem(position);
        mListener.onListSelected(list, mMineName);
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void fetchLists() {
        GetListsRequest request = new GetListsRequest(getActivity(), mMineName, null);
        executeRequest(request, new GetListsRequestListener());
    }

    protected void setProgress(boolean loading) {
        if (loading) {
            Views.setVisible(mProgressView);
            Views.setGone(mListsView);
        } else {
            Views.setVisible(mListsView);
            Views.setGone(mProgressView);
        }
    }

    public void setMineName(String mineName) {
        mMineName = mineName;
    }
}
