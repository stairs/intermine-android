package org.intermine.app.core;

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

