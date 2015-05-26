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