package org.intermine.core.templates.constraint;

public class PathConstraintLookup extends PathConstraint {
    private String mValue;
    private String mExtraValue;

    public PathConstraintLookup(String path, String value, String extraValue) {
        super(path, ConstraintOperation.LOOKUP);
        if (value == null) {
            throw new NullPointerException("Cannot create a lookup constraint with a null value");
        }
        mValue = value;
        mExtraValue = extraValue;
    }

    public String getValue() {
        return mValue;
    }

    public String getExtraValue() {
        return mExtraValue;
    }

    @Override
    public String toString() {
        return mPath + " LOOKUP " + mValue + " IN " + mExtraValue;
    }
}
