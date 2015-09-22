package org.intermine.app.web; 
/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class BaseWebClient extends WebViewClient {
    private static final String TAG = BaseWebClient.class.getSimpleName();

    private static final String SCHEME_HTTP = "http:";

    private boolean pageLoading;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(SCHEME_HTTP)) {
            return false;
        }
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (!pageLoading) {
            pageLoading = true;
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (pageLoading) {
            pageLoading = false;
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.w(TAG, description);

        if (pageLoading) {
            pageLoading = false;
        }
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        view.goBack();
    }
}