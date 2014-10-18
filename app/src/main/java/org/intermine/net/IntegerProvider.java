package org.intermine.net;

import com.google.inject.Provider;

/**
 * @author Siarhei Ivonchik <Siarhei_Ivonchik @ epam.com>
 */
public class IntegerProvider implements Provider<Integer> {
    @Override
    public Integer get() {
        return Integer.valueOf(0);
    }
}
