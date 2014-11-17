package org.intermine.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.R;
import org.intermine.core.model.Model;
import org.intermine.net.request.get.GetModelRequest;
import org.intermine.util.Mines;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;


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

//        if (null == getStorage().getMineToModelMap()) {
//            setContentView(R.layout.start_activity);
//            Map<String, String> mineToBaseUrlMap = Mines.getMineToBaseUrlMap(this);
//
//            mCountDownLatch = new CountDownLatch(mineToBaseUrlMap.size());
//            new WailForAllRequestsFinishedAsyncTask().execute();
//
//            for (String mineBaseUrl : mineToBaseUrlMap.values()) {
//                executeRequest(new GetModelRequest(this, mineBaseUrl), new ModelRequestListener());
//            }
//        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                }
            }, 1000);
//        }
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