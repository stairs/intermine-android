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

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.retry.RetryPolicy;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
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
