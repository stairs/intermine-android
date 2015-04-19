package org.intermine.app.core.templates.constraint;

import org.intermine.app.util.Collections;

import java.util.Collection;

public class PathConstraintMultiValue extends PathConstraint {
    private Collection<String> mValues;

    public PathConstraintMultiValue(String path, ConstraintOperation operation,
                                    Collection<String> values, String code) {
        super(path, operation, code);

        if (Collections.isNullOrEmpty(values)) {
            throw new IllegalArgumentException("Multivalue constraint's values should not be empty!");
        }
        this.mValues = values;
    }

    public Collection<String> getValues() {
        return mValues;
    }
}