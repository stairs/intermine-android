package org.intermine.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.activity.MainActivity;
import org.intermine.adapter.TemplatesAdapter;
import org.intermine.core.templates.Template;
import org.intermine.net.request.get.GetTemplatesRequest;
import org.intermine.net.request.get.GetTemplatesRequest.Templates;
import org.intermine.util.Views;
import org.intermine.view.ProgressView;

public class TemplatesFragment extends BaseFragment {
    protected ListView mTemplates;
    private TemplatesAdapter mTemplatesAdapter;

    protected View mNotFoundView;
    private ProgressView mProgressView;

    private OnTemplateSelectedListener mOnTemplateSelectedListener;

    protected boolean mLoading;


    public TemplatesFragment() {
    }

    public static TemplatesFragment newInstance() {
        TemplatesFragment fragment = new TemplatesFragment();
        return fragment;
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    public static interface OnTemplateSelectedListener {
        void onTemplateSelected(Template template);
    }

    public class GetTemplatesListener implements RequestListener<Templates> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            setProgress(false);
            Toast.makeText(getActivity(), spiceException.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onRequestSuccess(Templates result) {
            setProgress(false);

            if (result == null || result.isEmpty()) {
                Views.setVisible(mNotFoundView);
                Views.setGone(mTemplates);
            } else {
                Views.setVisible(mTemplates);
                Views.setGone(mNotFoundView);

                mTemplatesAdapter.updateData(result.values());
            }
        }
    }
    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.templates_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressView = (ProgressView) view.findViewById(R.id.progress_view);
        mNotFoundView = view.findViewById(R.id.not_found_results_container);

        mTemplates = (ListView) view.findViewById(R.id.templates);
        mTemplatesAdapter = new TemplatesAdapter(getActivity());
        mTemplates.setAdapter(mTemplatesAdapter);

        mTemplates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != mOnTemplateSelectedListener) {
                    Template selected = (Template) mTemplatesAdapter.getItem(position);
                    mOnTemplateSelectedListener.onTemplateSelected(selected);
                }
            }
        });

        setProgress(true);
        performGetTemplates();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnTemplateSelectedListener = (OnTemplateSelectedListener) activity;

        ((MainActivity) activity).onSectionAttached(getString(R.string.templates));
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------


    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------
    protected void performGetTemplates() {
        GetTemplatesRequest request = new GetTemplatesRequest(getActivity());
        executeRequest(request, new GetTemplatesListener());
    }

    protected void setProgress(boolean loading) {
        mLoading = loading;

        if (loading) {
            Views.setVisible(mProgressView);
            Views.setGone(mTemplates, mNotFoundView);
        } else {
            Views.setVisible(mTemplates);
            Views.setGone(mProgressView);
        }
    }
}
