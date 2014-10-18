package org.intermine.core;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GeneInfo {
    @SerializedName("id")
    private String mId;
    @SerializedName("relevance")
    private double mRelevance;
    @SerializedName("type")
    private String mType;
    @SerializedName("fields")
    private Map<String, String> mFields;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public double getRelevance() {
        return mRelevance;
    }

    public void setRelevance(double relevance) {
        mRelevance = relevance;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public Map<String, String> getFields() {
        return mFields;
    }

    public void setFields(Map<String, String> fields) {
        mFields = fields;
    }
}
