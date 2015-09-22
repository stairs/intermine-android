package org.intermine.app.activity; 
/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.intermine.app.R;
import org.intermine.app.util.Strs;
import org.intermine.app.util.Views;
import org.intermine.app.web.BaseWebClient;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class WebActivity extends BaseActivity {
    public static final String LOAD_URL_EXTRA = "load_url";
    public static final String TITLE_EXTRA = "title";

    protected String mUrl;
    protected String mTitle;
    @InjectView(R.id.web_container)
    WebView mWebView;
    @InjectView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @InjectView(R.id.default_toolbar)
    Toolbar mToolbar;
    private Client mWebClient;

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    public static void start(Activity activity, String title, String url) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra(LOAD_URL_EXTRA, url);
        intent.putExtra(TITLE_EXTRA, title);
        activity.startActivity(intent);
        activity.finish();
    }

    // --------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mUrl == null) {
            mUrl = getIntent().getStringExtra(LOAD_URL_EXTRA);
        }

        if (mTitle == null) {
            mTitle = getIntent().getStringExtra(TITLE_EXTRA);
        }

        initContent();
        setUpWebViewClients();
        mWebView.loadUrl(mUrl);
    }

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    protected void initContent() {
        if (!Strs.isNullOrEmpty(mTitle)) {
            setTitle(mTitle);
        }
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    // --------------------------------------------------------------------------------------------
    // Activity Lifecycle
    // --------------------------------------------------------------------------------------------

    private void setUpWebViewClients() {
        mWebClient = new Client();
        mWebView.setWebViewClient(mWebClient);
    }
    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    private void setLoading(boolean loading) {
        if (loading) {
            Views.setInvisible(mWebView);
            Views.setVisible(mProgressBar);
        } else {
            Views.setVisible(mWebView);
            Views.setGone(mProgressBar);
        }
    }

    private class Client extends BaseWebClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            setLoading(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            setLoading(false);
        }
    }
}