package org.intermine.core.templates.constraint;

import org.intermine.util.Collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.intermine.core.templates.constraint.ConstraintOperation.NONE_OF;
import static org.intermine.core.templates.constraint.ConstraintOperation.ONE_OF;

public abstract class PathConstraintMultiValue extends PathConstraint {
    public static final Set<ConstraintOperation> VALID_OPERATIONS = new HashSet<>(Arrays.asList(
            ONE_OF, NONE_OF));

    private Collection<String> mValues;

    public PathConstraintMultiValue(String path, ConstraintOperation operation, Collection<String> values) {
        super(path, operation);

        if (Collections.isNullOrEmpty(values)) {
            throw new IllegalArgumentException("Multivalue constraint's values should not be empty!");
        }
        this.mValues = values;
    }

    public Collection<String> getValues() {
        return mValues;
    }
}