package org.intermine.net;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.retry.RetryPolicy;

/**
 * @author Siarhei Ivonchik <Siarhei_Ivonchik @ epam.com>
 */
public class NoRetryPolicy implements RetryPolicy {
    @Override
    public long getDelayBeforeRetry() {
        return 0;
    }

    @Override
    public int getRetryCount() {
        return 0;
    }

    @Override
    public void retry(SpiceException ex) { }
}
