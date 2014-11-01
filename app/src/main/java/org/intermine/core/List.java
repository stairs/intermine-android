package org.intermine.core;

import com.google.gson.annotations.SerializedName;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class List {
    @SerializedName("title")
    private String mTitle;
    @SerializedName("name")
    private String mName;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("type")
    private String mType;
    @SerializedName("size")
    private int mSize;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof List)) return false;

        List list = (List) o;

        if (mSize != list.mSize) return false;
        if (mDescription != null ? !mDescription.equals(list.mDescription) : list.mDescription != null)
            return false;
        if (mName != null ? !mName.equals(list.mName) : list.mName != null) return false;
        if (mTitle != null ? !mTitle.equals(list.mTitle) : list.mTitle != null) return false;
        if (mType != null ? !mType.equals(list.mType) : list.mType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mTitle != null ? mTitle.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        result = 31 * result + (mType != null ? mType.hashCode() : 0);
        result = 31 * result + mSize;
        return result;
    }

    @Override
    public String toString() {
        return "List{" +
                "mTitle='" + mTitle + '\'' +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mType='" + mType + '\'' +
                ", mSize=" + mSize +
                '}';
    }
}
