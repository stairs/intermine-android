package org.intermine.core.templates.constraint;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.intermine.core.templates.constraint.ConstraintOperation.*;

public class PathConstraintRange extends PathConstraintMultiValue {

    public static final Set<ConstraintOperation> VALID_OPERATIONS = new HashSet<>(Arrays.asList(
            WITHIN, OUTSIDE, OVERLAPS, DOES_NOT_OVERLAP, CONTAINS, DOES_NOT_CONTAIN));

    public PathConstraintRange(String path, ConstraintOperation operation, Collection<String> ranges) {
        super(path, operation, ranges);

        checkValidOperation(VALID_OPERATIONS, operation);
    }
}