package org.intermine.app.net;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.retry.RetryPolicy;

import org.intermine.app.util.Strs;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

/**
 * Default retry policy for Robospice requests. In default mode behaves as {@code NoRetryPolicy}.
 * In case of Internal Server Error with an empty body received switches to exceptional retry count
 * and delay parameters.
 */
public class DefaultRetryPolicy implements RetryPolicy {
    public static final int EXCEPTIONAL_MODE_RETRY_COUNT = 3;
    public static final long EXCEPTIONAL_MODE_DELAY_BEFORE_RETRY = 5000;

    /**
     * The number of retry attempts.
     */
    private int mRetryCount = 0;

    /**
     * The delay between retry attempts.
     */
    private long mDelayBeforeRetry = 0;

    private boolean mInDefaultMode = true;

    @Override
    public int getRetryCount() {
        return mRetryCount;
    }

    @Override
    public long getDelayBeforeRetry() {
        return mDelayBeforeRetry;
    }

    @Override
    public void retry(SpiceException ex) {
        if (mInDefaultMode && exceptionalModeRequired(ex)) {
            mInDefaultMode = false;
            adjustPolicy2Mode();
        } else {
            mRetryCount--;

            if (0 == mRetryCount) {
                mInDefaultMode = true;
                adjustPolicy2Mode();
            }
        }
    }

    /**
     * Checks whether Internal Server Error with an empty body received.
     */
    protected boolean exceptionalModeRequired(SpiceException ex) {
        HttpNetworkException netEx;

        try {
            netEx = (HttpNetworkException) ex.getCause();

            if (null == netEx) {
                throw new ClassCastException();
            }
        } catch (ClassCastException e) {
            return false;
        }

        String httpMsg = netEx.getErrorMessage();
        return isExceptionalStatusCode(netEx.getStatusCode()) && Strs.isNullOrEmpty(httpMsg);
    }

    protected boolean isExceptionalStatusCode(HttpStatus code) {
        return INTERNAL_SERVER_ERROR == code || SERVICE_UNAVAILABLE == code;
    }

    protected void adjustPolicy2Mode() {
        if (mInDefaultMode) {
            mRetryCount = 0;
            mDelayBeforeRetry = 0;
        } else {
            mRetryCount = EXCEPTIONAL_MODE_RETRY_COUNT;
            mDelayBeforeRetry = EXCEPTIONAL_MODE_DELAY_BEFORE_RETRY;
        }
    }
}
