package org.intermine.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.app.InterMineApplication;
import org.intermine.app.R;
import org.intermine.app.dialog.AlertDialogFragment;
import org.intermine.app.storage.Storage;
import org.intermine.app.util.Views;

import javax.inject.Inject;

public abstract class BaseActivity extends ActionBarActivity implements
        AlertDialogFragment.OnDialogDismissedListener {
    public static final int UNAUTHORIZED_CODE = 0x3482;

    @Inject
    SpiceManager mSpiceManager;

    @Inject
    Storage mStorage;

    // --------------------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InterMineApplication app = InterMineApplication.get(this);
        app.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSpiceManager.start(this);
    }

    @Override
    protected void onStop() {
        try {
            if (mSpiceManager.isStarted()) {
                mSpiceManager.shouldStop();
            }
        } finally {
            super.onStop();
        }
    }

    // --------------------------------------------------------------------------------------------
    // Callback Methods
    // --------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogDismissed(int code) {

    }

    // --------------------------------------------------------------------------------------------
    // Helper Mehods
    // --------------------------------------------------------------------------------------------

    public void showStandardAlert(int messageId, int code) {
        String message = getString(messageId);
        showStandardAlert(null, message, code);
    }

    public void showStandardAlert(String message, int code) {
        showStandardAlert(null, message, code);
    }

    public void showStandardAlert(String title, int messageId, int code) {
        String message = getString(messageId);
        showStandardAlert(null, message, code);
    }

    public void showStandardAlert(String title, String message, int code) {
        Views.showDialogFragment(getFragmentManager(), AlertDialogFragment.newInstance(code, message));
    }

    public <T> void execute(SpiceRequest<T> request, RequestListener<T> listener) {
        mSpiceManager.execute(request, listener);
    }

    public <T> void execute(SpiceRequest<T> request, Object requestCacheKey,
                            long cacheExpiryDuration, RequestListener<T> listener) {
        mSpiceManager.execute(request, requestCacheKey, cacheExpiryDuration, listener);
    }

    public Storage getStorage() {
        return mStorage;
    }
}
