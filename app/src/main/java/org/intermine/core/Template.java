package org.intermine.core;

import com.google.gson.annotations.SerializedName;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class Template {
    @SerializedName("title")
    private String mTitle;
    @SerializedName("description")
    private String mDescription;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}

