package org.intermine.app.core.templates.constraint;

public class PathConstraintLookup extends PathConstraint {
    private String mValue;
    private String mExtraValue;

    public PathConstraintLookup(String path, String value, String extraValue, String code) {
        super(path, ConstraintOperation.LOOKUP, code);

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
}