package org.intermine.app.net;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.util.Log;

import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.exception.RequestCancelledException;
import com.octo.android.robospice.persistence.exception.SpiceException;

import org.intermine.app.R;
import org.intermine.app.activity.BaseActivity;
import org.intermine.app.storage.Storage;
import org.intermine.app.util.Strs;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class ResponseHelper {
    public static final String TAG = ResponseHelper.class.getSimpleName();
    public static final int DIALOG_CODE_NETWORK = 0x3433;

    public static void handleSpiceException(SpiceException ex, BaseActivity atv,
                                            String mineName, int dialogCode) {
        if (ex instanceof RequestCancelledException) {
            return;
        }

        if (ex instanceof NoNetworkException) {
            atv.showStandardAlert(R.string.no_network_error_message, DIALOG_CODE_NETWORK);
            return;
        }

        HttpNetworkException networkException;
        try {
            networkException = (HttpNetworkException) ex.getCause();

            if (null == networkException) {
                throw new ClassCastException();
            }
        } catch (ClassCastException e) {
            atv.showStandardAlert(R.string.unknown_error_message, dialogCode);
            return;
        }

        HttpStatus code = networkException.getStatusCode();
        String message = networkException.getErrorMessage();

        // Handle unauthorized
        if (UNAUTHORIZED == code) {
            Storage storage = atv.getStorage();
            storage.setUserToken(mineName, null);

            atv.showStandardAlert(R.string.unauthorized_error_message, BaseActivity.UNAUTHORIZED_CODE);
            return;
        }

        if (Strs.isNullOrEmpty(message)) {
            atv.showStandardAlert(R.string.unknown_error_message, dialogCode);
        } else {
            atv.showStandardAlert(message, dialogCode);
        }

        Log.e(TAG, ex.toString());
    }

    public static void handleSpiceException(SpiceException ex, BaseActivity atv, String mineName) {
        handleSpiceException(ex, atv, mineName, DIALOG_CODE_NETWORK);
    }
}
