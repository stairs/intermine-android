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

public class PathConstraintLookup extends PathConstraint {
    private String mValue;
    private String mExtraValue;

    public PathConstraintLookup(String path, String value, String extraValue, String code) {
        super(path, ConstraintOperation.LOOKUP, code);

        if (value == null) {
            throw new NullPointerException("Cannot create a lookup constraint with a null value");
        }
        mValue = value;
        mExtraValue = extraValue;
    }

    public String getValue() {
        return mValue;
    }

    public String getExtraValue() {
        return mExtraValue;
    }
}