package org.intermine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.InterMineApplication;
import org.intermine.R;
import org.intermine.dialog.AlertDialogFragment;
import org.intermine.service.RoboSpiceService;
import org.intermine.storage.Storage;
import org.intermine.util.Views;

import javax.inject.Inject;

public abstract class BaseActivity extends ActionBarActivity implements
        AlertDialogFragment.OnDialogDismissedListener {
    private final SpiceManager mSpiceManager = new SpiceManager(RoboSpiceService.class);

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
    public void onDialogDismissed(int id) {

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


    public void executeRequest(SpiceRequest request, RequestListener listener) {
        mSpiceManager.execute(request, listener);
    }

    public Storage getStorage() {
        return mStorage;
    }
}
