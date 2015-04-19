package org.intermine.app.core.templates.constraint;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Constraint implements Parcelable {
    @SerializedName("path")
    private String mPath;

    @SerializedName("op")
    private String mOperation;

    @SerializedName("editable")
    private boolean mEditable;

    @SerializedName("code")
    private String mCode;

    @SerializedName("switchable")
    private boolean mSwitchable;

    @SerializedName("switched")
    private String mSwitched;

    @SerializedName("value")
    private String mValue;

    @SerializedName("extraValue")
    private String mExtraValue;

    @SerializedName("values")
    private List<String> mValues;

    public Constraint(Parcel in) {
        mPath = in.readString();
        mOperation = in.readString();
        mEditable = Boolean.parseBoolean(in.readString());
        mCode = in.readString();
        mSwitchable = Boolean.parseBoolean(in.readString());
        mSwitched = in.readString();
        mValue = in.readString();
        mExtraValue = in.readString();
        mValues = in.readArrayList(null);
    }

    public Constraint() {

    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getOperation() {
        return mOperation;
    }

    public void setOperation(String operation) {
        mOperation = operation;
    }

    public boolean isEditable() {
        return mEditable;
    }

    public void setEditable(boolean editable) {
        mEditable = editable;
    }

    public boolean isSwitchable() {
        return mSwitchable;
    }

    public void setSwitchable(boolean switchable) {
        mSwitchable = switchable;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getSwitched() {
        return mSwitched;
    }

    public void setSwitched(String switched) {
        mSwitched = switched;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public List<String> getValues() {
        return mValues;
    }

    public void setValues(List<String> values) {
        mValues = values;
    }

    public String getExtraValue() {
        return mExtraValue;
    }

    public void setExtraValue(String extraValue) {
        mExtraValue = extraValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPath);
        dest.writeString(mOperation);
        dest.writeString(Boolean.toString(mEditable));
        dest.writeString(mCode);
        dest.writeString(Boolean.toString(mSwitchable));
        dest.writeString(mSwitched);
        dest.writeString(mValue);
        dest.writeString(mExtraValue);
        dest.writeList(mValues);
    }

    public static final Creator<Constraint> CREATOR = new Creator<Constraint>() {
        @Override
        public Constraint createFromParcel(Parcel in) {
            return new Constraint(in);
        }

        @Override
        public Constraint[] newArray(int size) {
            return new Constraint[size];
        }
    };
}