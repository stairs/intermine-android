package org.intermine.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.InterMineApplication;
import org.intermine.service.RoboSpiceService;
import org.intermine.storage.Storage;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class BaseFragment extends Fragment {
    protected SpiceManager mSpiceManager = new SpiceManager(RoboSpiceService.class);
    private Storage mStorage;

    // --------------------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InterMineApplication app = (InterMineApplication) getActivity().getApplication();
        mStorage = app.getStorage();
    }

    @Override
    public void onStart() {
        super.onStart();
        mSpiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        try {
            if (null != mSpiceManager && mSpiceManager.isStarted()) {
                mSpiceManager.shouldStop();
            }
        } finally {
            super.onStop();
        }
    }

    // --------------------------------------------------------------------------------------------
    // Helper Mehods
    // --------------------------------------------------------------------------------------------

    protected void executeRequest(SpiceRequest request, RequestListener listener) {
        mSpiceManager.execute(request, listener);
    }

    public Storage getStorage() {
        return mStorage;
    }
}
