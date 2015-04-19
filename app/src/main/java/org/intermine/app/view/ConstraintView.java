package org.intermine.app.view;

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
