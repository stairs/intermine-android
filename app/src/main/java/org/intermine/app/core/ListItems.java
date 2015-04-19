package org.intermine.app.core;

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
