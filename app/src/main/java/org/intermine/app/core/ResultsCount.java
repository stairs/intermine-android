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

import com.google.gson.annotations.SerializedName;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class ResultsCount {
    @SerializedName("count")
    private int mCount;

    public ResultsCount() {
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }
}

