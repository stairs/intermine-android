package org.intermine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.adapter.ListAdapter;
import org.intermine.controller.LoadOnScrollViewController;
import org.intermine.core.ListItems;
import org.intermine.core.templates.Template;
import org.intermine.adapter.ApiPager;
import org.intermine.net.request.get.GetTemplateResultsRequest;
import org.intermine.util.Collections;
import org.intermine.util.Views;
import org.intermine.view.ProgressView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class TemplateResultsActivity extends BaseActivity {
    public static final int ITEMS_PER_PAGE = 15;

    public static final String TEMPLATE_KEY = "template_key";
    public static final String MINE_NAME_KEY = "mine_name_key";

    @InjectView(R.id.list)
    ListView mListView;

    @InjectView(R.id.progress_view)
    ProgressView mProgressView;

    @InjectView(R.id.not_found_results_container)
    View mNotFoundView;

    private ListAdapter mListAdapter;

    protected LoadOnScrollViewController mViewController;
    private LoadOnScrollViewController.LoadOnScrollDataController mDataController;
    private ApiPager mPager;

    private Template mTemplate;
    private String mMineName;

    protected boolean mLoading;

    // --------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------

    public static void start(Context context, Template template, String mineName) {
        Intent intent = new Intent(context, TemplateResultsActivity.class);
        intent.putExtra(TEMPLATE_KEY, template);
        intent.putExtra(MINE_NAME_KEY, mineName);
        context.startActivity(intent);
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    public class TemplateResultsListener implements RequestListener<ListItems> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            setProgress(false);
            mViewController.onFinishLoad();

            Views.setVisible(mNotFoundView);
        }

        @Override
        public void onRequestSuccess(ListItems result) {
            setProgress(false);
            mViewController.onFinishLoad();

            if (null != result && !Collections.isNullOrEmpty(result.getFeatures())) {
                mListAdapter.updateData(result);
            } else {
                Views.setVisible(mNotFoundView);
            }
        }
    }

    public class TemplateResultsCountListener implements RequestListener<Integer> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            setProgress(false);
            mViewController.onFinishLoad();

            Views.setVisible(mNotFoundView);
        }

        @Override
        public void onRequestSuccess(Integer count) {
            mPager = new ApiPager(count, 0, ITEMS_PER_PAGE);
            fetchTemplateResults();
        }
    }
    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_results_activity);
        ButterKnife.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTemplate = getIntent().getParcelableExtra(TEMPLATE_KEY);
        mMineName = getIntent().getStringExtra(MINE_NAME_KEY);

        mListAdapter = new ListAdapter(this);
        mListView.setAdapter(mListAdapter);

        mViewController = new LoadOnScrollViewController(getDataController(), this);
        mListView.setOnScrollListener(mViewController);
        mListView.addFooterView(mViewController.getFooterView());

        if (null != mTemplate) {
            setTitle(mTemplate.getTitle());
            setProgress(true);

            fetchTemplateResultsCount();
        }
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
                mPager = mPager.next();
                fetchTemplateResults();

                mViewController.onStartLoad();
                mLoading = true;
            }
        };
    }

    protected void fetchTemplateResults() {
        GetTemplateResultsRequest request = new GetTemplateResultsRequest(ListItems.class, this,
                mTemplate, mMineName, mPager.getCurrentPage() * mPager.getPerPage(), ITEMS_PER_PAGE);
        executeRequest(request, new TemplateResultsListener());
    }

    protected void fetchTemplateResultsCount() {
        GetTemplateResultsRequest request = new GetTemplateResultsRequest(Integer.class, this,
                mTemplate, mMineName, 0, ITEMS_PER_PAGE);
        executeRequest(request, new TemplateResultsCountListener());
    }

    protected void setProgress(boolean loading) {
        mLoading = loading;

        if (loading) {
            Views.setVisible(mProgressView);
            Views.setGone(mListView, mNotFoundView);
        } else {
            Views.setVisible(mListView);
            Views.setGone(mProgressView);
        }
    }
}
