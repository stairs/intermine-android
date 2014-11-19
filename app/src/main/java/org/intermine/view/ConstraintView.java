package org.intermine.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import org.intermine.R;
import org.intermine.adapter.SimpleAdapter;
import org.intermine.core.templates.constraint.ConstraintOperation;
import org.intermine.core.templates.constraint.PathConstraint;
import org.intermine.core.templates.constraint.PathConstraintAttribute;

import butterknife.ButterKnife;
import butterknife.InjectView;

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

    public PathConstraint getPathConstraint() {
        return mPathConstraint;
    }

    public void setPathConstraint(PathConstraint pathConstraint) {
        mPathConstraint = pathConstraint;
    }
}
