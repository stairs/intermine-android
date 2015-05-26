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

import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class ListItems {
    @SerializedName("columnHeaders")
    private List<String> mFeaturesNames;
    @SerializedName("results")
    private List<List<String>> mFeatures;

    public List<String> getFeaturesNames() {
        return mFeaturesNames;
    }

    public void setFeaturesNames(List<String> featuresNames) {
        mFeaturesNames = featuresNames;
    }

    public List<List<String>> getFeatures() {
        return mFeatures;
    }

    public void setFeatures(List<List<String>> features) {
        mFeatures = features;
    }
}
