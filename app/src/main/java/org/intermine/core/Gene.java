package org.intermine.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class Gene implements Parcelable, Comparable<Gene> {
    private long mId;
    private String mPrimaryDBId;
    private String mSymbol;
    private String mDescription;
    private String mLocationStart;
    private String mLocationEnd;
    private String mLocationStrand;
    private String mLocatedOn;
    private String mName;
    private String mOrganismName;
    private String mOrganismShortName;
    private String mSecondaryIdentifier;
    private String mOntologyTerm;
    private String mMine;
    private double mRelevance;

    public Gene() {
    }

    public Gene(Parcel in) {
        mId = in.readLong();
        String[] data = new String[13];
        in.readStringArray(data);

        mPrimaryDBId = data[0];
        mSymbol = data[1];
        mDescription = data[2];
        mLocationStart = data[3];
        mLocationEnd = data[4];
        mLocationStrand = data[5];
        mLocatedOn = data[6];
        mName = data[7];
        mSecondaryIdentifier = data[8];
        mOrganismName = data[9];
        mOrganismShortName = data[10];
        mOntologyTerm = data[11];
        mMine = data[12];
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getPrimaryDBId() {
        return mPrimaryDBId;
    }

    public void setPrimaryDBId(String primaryDBId) {
        mPrimaryDBId = primaryDBId;
    }

    public String getOrganismName() {
        return mOrganismName;
    }

    public void setOrganismName(String organismName) {
        mOrganismName = organismName;
    }

    public String getOrganismShortName() {
        return mOrganismShortName;
    }

    public void setOrganismShortName(String organismShortName) {
        mOrganismShortName = organismShortName;
    }

    public String getSecondaryIdentifier() {
        return mSecondaryIdentifier;
    }

    public void setSecondaryIdentifier(String secondaryIdentifier) {
        mSecondaryIdentifier = secondaryIdentifier;
    }

    public String getMine() {
        return mMine;
    }

    public void setMine(String mine) {
        mMine = mine;
    }

    public String getSymbol() {
        return mSymbol;
    }

    public void setSymbol(String symbol) {
        mSymbol = symbol;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getLocationStart() {
        return mLocationStart;
    }

    public void setLocationStart(String locationStart) {
        mLocationStart = locationStart;
    }

    public String getLocationEnd() {
        return mLocationEnd;
    }

    public void setLocationEnd(String locationEnd) {
        mLocationEnd = locationEnd;
    }

    public String getLocationStrand() {
        return mLocationStrand;
    }

    public void setLocationStrand(String locationStrand) {
        mLocationStrand = locationStrand;
    }

    public String getLocatedOn() {
        return mLocatedOn;
    }

    public void setLocatedOn(String locatedOn) {
        mLocatedOn = locatedOn;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getOntologyTerm() {
        return mOntologyTerm;
    }

    public void setOntologyTerm(String ontologyTerm) {
        mOntologyTerm = ontologyTerm;
    }

    public double getRelevance() {
        return mRelevance;
    }

    public void setRelevance(double relevance) {
        mRelevance = relevance;
    }

    @Override
    public String toString() {
        return "Gene{" +
                "mId=" + mId +
                ", mPrimaryDBId='" + mPrimaryDBId + '\'' +
                ", mSymbol='" + mSymbol + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mLocationStart='" + mLocationStart + '\'' +
                ", mLocationEnd='" + mLocationEnd + '\'' +
                ", mLocationStrand='" + mLocationStrand + '\'' +
                ", mLocatedOn='" + mLocatedOn + '\'' +
                ", mName='" + mName + '\'' +
                ", mOrganismName='" + mOrganismName + '\'' +
                ", mOrganismShortName='" + mOrganismShortName + '\'' +
                ", mSecondaryIdentifier='" + mSecondaryIdentifier + '\'' +
                ", mOntologyTerm='" + mOntologyTerm + '\'' +
                ", mMine='" + mMine + '\'' +
                ", mRelevance=" + mRelevance +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(Gene another) {
        if (getRelevance() > another.getRelevance())
            return -1;       // Neither val is NaN, thisVal is smaller
        if (getRelevance() < another.getRelevance())
            return 1;        // Neither val is NaN, thisVal is larger

        long thisBits = Double.doubleToLongBits(getRelevance());
        long anotherBits = Double.doubleToLongBits(getRelevance());

        return (thisBits == anotherBits ? 0 : // Values are equal
                (thisBits < anotherBits ? 1 : // (-0.0, 0.0) or (!NaN, NaN)
                        -1));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeStringArray(new String[]{mPrimaryDBId, mSymbol, mDescription, mLocationStart, mLocationEnd,
                mLocationStrand, mLocatedOn, mName, mSecondaryIdentifier, mOrganismName,
                mOrganismShortName, mOntologyTerm, mMine});
    }

    public static final Parcelable.Creator<Gene> CREATOR = new Parcelable.Creator<Gene>() {

        @Override
        public Gene createFromParcel(Parcel source) {
            return new Gene(source);
        }

        @Override
        public Gene[] newArray(int size) {
            return new Gene[size];
        }
    };
}
