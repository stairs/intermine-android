package org.intermine.app.fragment;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import org.intermine.app.InterMineApplication;
import org.intermine.app.storage.Storage;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public abstract class BaseFragment extends Fragment {
    @Inject
    SpiceManager mSpiceManager;

    @Inject
    Storage mStorage;

    // --------------------------------------------------------------------------------------------
    // Lifecycle
    // --------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InterMineApplication app = InterMineApplication.get(getActivity());
        app.inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    // --------------------------------------------------------------------------------------------
    // Helper Mehod
    // --------------------------------------------------------------------------------------------

    protected <T> void execute(SpiceRequest<T> request, RequestListener<T> listener) {
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
