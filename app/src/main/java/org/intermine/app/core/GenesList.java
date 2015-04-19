package org.intermine.app.core;

import java.util.ArrayList;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GenesList extends ArrayList<Gene> {
    private int mResultsCount;

    public int getResultsCount() {
        return mResultsCount;
    }

    public void setResultsCount(int resultsCount) {
        mResultsCount = resultsCount;
    }
}
