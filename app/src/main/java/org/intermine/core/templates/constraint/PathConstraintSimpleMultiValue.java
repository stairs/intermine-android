package org.intermine.core.templates.constraint;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.intermine.core.templates.constraint.ConstraintOperation.NONE_OF;
import static org.intermine.core.templates.constraint.ConstraintOperation.ONE_OF;

public class PathConstraintSimpleMultiValue extends PathConstraintMultiValue {
    public static final Set<ConstraintOperation> VALID_OPERATIONS = new HashSet<>(Arrays.asList(
            ONE_OF, NONE_OF));

    public PathConstraintSimpleMultiValue(String path, ConstraintOperation operation,
                                          Collection<String> values, String code) {
        super(path, operation, values, code);
        checkValidOperation(VALID_OPERATIONS, mOperation);
    }
}