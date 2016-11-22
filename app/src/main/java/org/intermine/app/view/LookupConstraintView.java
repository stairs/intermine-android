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
import android.widget.EditText;

import org.intermine.app.R;
import org.intermine.app.core.templates.constraint.PathConstraint;
import org.intermine.app.core.templates.constraint.PathConstraintLookup;
import org.intermine.app.util.Strs;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class LookupConstraintView extends ConstraintView {
    @BindView(R.id.lookup_value)
    EditText mValue;

    public LookupConstraintView(Context context, PathConstraintLookup constraint) {
        super(context, constraint);

        init();
        mValue.setText(constraint.getValue());
    }

    public LookupConstraintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LookupConstraintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.lookup_constraint_view, this);
        ButterKnife.bind(this);
    }

    @Override
    public boolean isValidConstraint() {
        return !Strs.isNullOrEmpty(mValue.getText().toString());
    }

    @Override
    public void highlightInvalid() {
        mValue.setError(getContext().getString(R.string.empty_value_em));
    }

    public String getValue() {
        return mValue.getText().toString();
    }

    @Override
    public PathConstraint getGeneratedConstraint() {
        PathConstraintLookup constraint = (PathConstraintLookup) getPathConstraint();
        return new PathConstraintLookup(constraint.getPath(), getValue(),
                null, constraint.getCode());
    }
}
