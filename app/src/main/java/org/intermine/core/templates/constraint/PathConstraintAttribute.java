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
    public static final Set<ConstraintOperation> VALID_OPERATIONS = new HashSet<>(Arrays.asList(
            DOES_NOT_MATCH, EQUALS, GREATER_THAN, GREATER_THAN_EQUALS, LESS_THAN,
            LESS_THAN_EQUALS, MATCHES, NOT_EQUALS, CONTAINS, DOES_NOT_CONTAIN, EXACT_MATCH,
            STRICT_NOT_EQUALS));

    private String mValue;

    public PathConstraintAttribute(String path, ConstraintOperation operation, String value,
                                   String code) {
        super(path, operation, code);

        checkValidOperation(VALID_OPERATIONS, operation);

        if (null != value) {
            throw new NullPointerException("Constraint's value can not be null!");
        }
        this.mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}