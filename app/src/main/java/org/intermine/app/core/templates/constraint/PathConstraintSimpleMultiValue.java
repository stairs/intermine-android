package org.intermine.app.core.templates.constraint;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.intermine.app.core.templates.constraint.ConstraintOperation.NONE_OF;
import static org.intermine.app.core.templates.constraint.ConstraintOperation.ONE_OF;

public class PathConstraintSimpleMultiValue extends PathConstraintMultiValue {
    public static final Set<ConstraintOperation> VALID_OPERATIONS = new HashSet<>(Arrays.asList(
            ONE_OF, NONE_OF));

    public PathConstraintSimpleMultiValue(String path, ConstraintOperation operation,
                                          Collection<String> values, String code) {
        super(path, operation, values, code);
        checkValidOperation(VALID_OPERATIONS, mOperation);
    }
}