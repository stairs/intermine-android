package org.intermine.fragment;

import android.app.Fragment;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.service.RoboSpiceService;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class BaseFragment extends Fragment {
    protected SpiceManager mSpiceManager = new SpiceManager(RoboSpiceService.class);

    // --------------------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------------------

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
}
