package org.intermine.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.activity.MainActivity;
import org.intermine.adapter.ListsAdapter;
import org.intermine.net.request.get.GetListsRequest;
import org.intermine.util.Views;
import org.intermine.view.ProgressView;

import java.util.List;

public class ListsFragment extends BaseFragment {
    public static final String TAG = ListsFragment.class.getSimpleName();

    private ProgressView mProgressView;
    private ListView mListsView;
    private ListsAdapter mAdapter;

    private OnListSelectedListener mListener;

    protected boolean mLoading;

    public ListsFragment() {
    }

    public static ListsFragment newInstance() {
        ListsFragment fragment = new ListsFragment();
        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    public static interface OnListSelectedListener {
        void onListSelected(org.intermine.core.List list);
    }

    class GetListsRequestListener implements RequestListener<GetListsRequest.Lists> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            setProgress(false);
            Log.e(TAG, spiceException.toString());
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
        View view = inflater.inflate(R.layout.lists_fragment, container, false);

        fetchLists();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressView = (ProgressView) view.findViewById(R.id.progress_view);
        mListsView = (ListView) view.findViewById(R.id.lists);

        mAdapter = new ListsAdapter(getActivity());
        mListsView.setAdapter(mAdapter);
        mListsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onListSelected((org.intermine.core.List) mAdapter.getItem(position));
            }
        });
        setProgress(true);
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


    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void fetchLists() {
        GetListsRequest request = new GetListsRequest(getActivity(), "FlyMine", null);
        executeRequest(request, new GetListsRequestListener());
    }

    protected void setProgress(boolean loading) {
        mLoading = loading;

        if (loading) {
            Views.setVisible(mProgressView);
            Views.setGone(mListsView);
        } else {
            Views.setVisible(mListsView);
            Views.setGone(mProgressView);
        }
    }
}
