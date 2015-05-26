package org.intermine.app.view;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.intermine.app.core.templates.constraint.PathConstraint;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public abstract class ConstraintView extends RelativeLayout {
    private PathConstraint mPathConstraint;

    public ConstraintView(Context context, PathConstraint pathConstraint) {
        super(context);
        mPathConstraint = pathConstraint;
    }

    public ConstraintView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConstraintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected PathConstraint getPathConstraint() {
        return mPathConstraint;
    }

    public abstract boolean isValidConstraint();

    public abstract void highlightInvalid();

    public abstract PathConstraint getGeneratedConstraint();
}
