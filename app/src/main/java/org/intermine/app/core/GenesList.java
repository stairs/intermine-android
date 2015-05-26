package org.intermine.app.core;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

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
