package org.intermine.net;

import com.google.inject.Provider;

/**
 * @author Siarhei Ivonchik <Siarhei_Ivonchik @ epam.com>
 */
public class DoubleProvider implements Provider<Double> {
    @Override
    public Double get() {
        return Double.valueOf(0.0);
    }
}
