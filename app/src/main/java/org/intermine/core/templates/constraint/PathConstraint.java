package org.intermine.core.templates.constraint;

public abstract class PathConstraint {
    protected String mPath;
    protected ConstraintOperation mOperation;

    protected PathConstraint(String path, ConstraintOperation operation) {
        mPath = path;
        mOperation = operation;
    }

    public String getPath() {
        return mPath;
    }

    public ConstraintOperation getOperation() {
        return mOperation;
    }

    public static String getValue(PathConstraint con) {
        if (con instanceof PathConstraintAttribute) {
            return ((PathConstraintAttribute) con).getValue();
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

//    public static Collection<String> getValues(PathConstraint con) {
//        if (con instanceof PathConstraintMultiValue) {
//            return ((PathConstraintMultiValue) con).getValues();
//        } else {
//            return null;
//        }
//    }
//
//    public static Collection<Integer> getIds(PathConstraint con) {
//        if (con instanceof PathConstraintIds) {
//            return ((PathConstraintIds) con).getIds();
//        } else {
//            return null;
//        }
//    }
}
