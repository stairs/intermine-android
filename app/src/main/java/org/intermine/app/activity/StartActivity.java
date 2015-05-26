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

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.app.R;
import org.intermine.app.core.model.Model;
import org.intermine.app.net.request.get.GetModelRequest;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class StartActivity extends BaseActivity {
    private static final String TAG = StartActivity.class.getSimpleName();

    private CountDownLatch mCountDownLatch;

    // --------------------------------------------------------------------------------------------
    // Inner Classes
    // --------------------------------------------------------------------------------------------

    class ModelRequestListener implements RequestListener<Model> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            mCountDownLatch.countDown();
            Log.e(TAG, spiceException.toString());
        }

        @Override
        public void onRequestSuccess(Model model) {
            mCountDownLatch.countDown();
            getStorage().addMineModel(model.getMineName(), model);
        }
    }

    private class WailForAllRequestsFinishedAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (0 < mCountDownLatch.getCount()) {
                try {
                    mCountDownLatch.await();
                } catch (InterruptedException e) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            startMainActivity();
        }
    }

    // --------------------------------------------------------------------------------------------
    // Activity Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();

        if (null == getStorage().getMineToModelMap()) {
            setContentView(R.layout.start_activity);
            Map<String, String> mineToBaseUrlMap = getStorage().getMineNameToUrlMap();

            mCountDownLatch = new CountDownLatch(mineToBaseUrlMap.size());
            new WailForAllRequestsFinishedAsyncTask().execute();

            for (String mineBaseUrl : mineToBaseUrlMap.values()) {
                execute(new GetModelRequest(this, mineBaseUrl), new ModelRequestListener());
            }
        } else {
            startMainActivity();
        }
    }


    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}