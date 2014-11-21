package org.intermine.core.templates.constraint;

import org.intermine.util.Strs;

import java.util.Collection;
import java.util.Set;

public abstract class PathConstraint {
    private String mCode;
    protected String mPath;
    protected ConstraintOperation mOperation;

    protected PathConstraint(String path, ConstraintOperation operation, String code) {
        if (Strs.isNullOrEmpty(path)) {
            throw new IllegalArgumentException("The path should not be empty!");
        }

        if (null == operation) {
            throw new NullPointerException("The operation for a PathConstraint can not be null!");
        }
        mPath = path;
        mCode = code;
        mOperation = operation;
    }

    public static String getValue(PathConstraint con) {
        if (con instanceof PathConstraintAttribute) {
            return ((PathConstraintAttribute) con).getValue();
        } else if (con instanceof PathConstraintLookup) {
            return ((PathConstraintLookup) con).getValue();
        }
        return null;
    }

    public static String getExtraValue(PathConstraint con) {
        if (con instanceof PathConstraintLookup) {
            return ((PathConstraintLookup) con).getExtraValue();
        } else {
            return null;
        }
    }

    public static Collection<String> getValues(PathConstraint con) {
        if (con instanceof PathConstraintMultiValue) {
            return ((PathConstraintMultiValue) con).getValues();
        } else {
            return null;
        }
    }

    protected void checkValidOperation(Set<ConstraintOperation> validOperations,
                                       ConstraintOperation operation) {
        if (!validOperations.contains(operation)) {
            String errorMessage = String.format("Invalid operation type for a %s!",
                    getClass().getSimpleName());
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public String getCode() {
        return mCode;
    }

    public String getPath() {
        return mPath;
    }

    public ConstraintOperation getOperation() {
        return mOperation;
    }
}