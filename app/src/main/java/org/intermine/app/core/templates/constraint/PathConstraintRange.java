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

import static org.intermine.app.core.templates.constraint.ConstraintOperation.CONTAINS;
import static org.intermine.app.core.templates.constraint.ConstraintOperation.DOES_NOT_CONTAIN;
import static org.intermine.app.core.templates.constraint.ConstraintOperation.DOES_NOT_OVERLAP;
import static org.intermine.app.core.templates.constraint.ConstraintOperation.OUTSIDE;
import static org.intermine.app.core.templates.constraint.ConstraintOperation.OVERLAPS;
import static org.intermine.app.core.templates.constraint.ConstraintOperation.WITHIN;

public class PathConstraintRange extends PathConstraintMultiValue {

    public static final Set<ConstraintOperation> VALID_OPERATIONS = new HashSet<>(Arrays.asList(
            WITHIN, OUTSIDE, OVERLAPS, DOES_NOT_OVERLAP, CONTAINS, DOES_NOT_CONTAIN));

    public PathConstraintRange(String path, ConstraintOperation operation, Collection<String> ranges,
                               String code) {
        super(path, operation, ranges, code);

        checkValidOperation(VALID_OPERATIONS, operation);
    }
}