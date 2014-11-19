package org.intermine.core.templates.constraint;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.intermine.core.templates.constraint.ConstraintOperation.CONTAINS;
import static org.intermine.core.templates.constraint.ConstraintOperation.DOES_NOT_CONTAIN;
import static org.intermine.core.templates.constraint.ConstraintOperation.DOES_NOT_OVERLAP;
import static org.intermine.core.templates.constraint.ConstraintOperation.OUTSIDE;
import static org.intermine.core.templates.constraint.ConstraintOperation.OVERLAPS;
import static org.intermine.core.templates.constraint.ConstraintOperation.WITHIN;

public class PathConstraintRange extends PathConstraintMultiValue {

    public static final Set<ConstraintOperation> VALID_OPERATIONS = new HashSet<>(Arrays.asList(
            WITHIN, OUTSIDE, OVERLAPS, DOES_NOT_OVERLAP, CONTAINS, DOES_NOT_CONTAIN));

    public PathConstraintRange(String path, ConstraintOperation operation, Collection<String> ranges,
                               String code) {
        super(path, operation, ranges, code);

        checkValidOperation(VALID_OPERATIONS, operation);
    }
}