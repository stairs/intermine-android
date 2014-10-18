package org.intermine.net;

import com.google.inject.Provider;

/**
 * @author Siarhei Ivonchik <Siarhei_Ivonchik @ epam.com>
 */
public class LongProvider implements Provider<Long> {
    @Override
    public Long get() {
        return Long.valueOf(0);
    }
}
