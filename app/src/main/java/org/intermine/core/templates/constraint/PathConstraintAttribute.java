package org.intermine.core.templates.constraint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.intermine.core.templates.constraint.ConstraintOperation.CONTAINS;
import static org.intermine.core.templates.constraint.ConstraintOperation.DOES_NOT_CONTAIN;
import static org.intermine.core.templates.constraint.ConstraintOperation.DOES_NOT_MATCH;
import static org.intermine.core.templates.constraint.ConstraintOperation.EQUALS;
import static org.intermine.core.templates.constraint.ConstraintOperation.EXACT_MATCH;
import static org.intermine.core.templates.constraint.ConstraintOperation.GREATER_THAN;
import static org.intermine.core.templates.constraint.ConstraintOperation.GREATER_THAN_EQUALS;
import static org.intermine.core.templates.constraint.ConstraintOperation.LESS_THAN;
import static org.intermine.core.templates.constraint.ConstraintOperation.LESS_THAN_EQUALS;
import static org.intermine.core.templates.constraint.ConstraintOperation.MATCHES;
import static org.intermine.core.templates.constraint.ConstraintOperation.NOT_EQUALS;
import static org.intermine.core.templates.constraint.ConstraintOperation.STRICT_NOT_EQUALS;

public class PathConstraintAttribute extends PathConstraint {

    public static final Set<ConstraintOperation> VALID_OPS = new HashSet<>(Arrays.asList(
            DOES_NOT_MATCH, EQUALS, GREATER_THAN, GREATER_THAN_EQUALS, LESS_THAN,
            LESS_THAN_EQUALS, MATCHES, NOT_EQUALS, CONTAINS, DOES_NOT_CONTAIN, EXACT_MATCH,
            STRICT_NOT_EQUALS));
    private String value;

    public PathConstraintAttribute(String path, ConstraintOperation operation, String value) {
        super(path, operation);
        if (operation == null) {
            throw new NullPointerException("Cannot construct a PathConstraintAttribute with a null"
                    + " op");
        }
        if (!VALID_OPS.contains(operation)) {
            throw new IllegalArgumentException("PathConstraints on attributes must use an op in"
                    + " the list \"" + VALID_OPS + "\"");
        }
        if (value == null) {
            throw new NullPointerException("Cannot create a constraint on a null value");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return mPath + " " + mOperation + " " + value;
    }
}
