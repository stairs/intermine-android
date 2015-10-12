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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.intermine.app.R;
import org.intermine.app.adapter.MultiValueListAdapter;
import org.intermine.app.adapter.SimpleAdapter;
import org.intermine.app.adapter.TemplatesAdapter;
import org.intermine.app.core.templates.constraint.Constraint;
import org.intermine.app.core.templates.constraint.ConstraintOperation;
import org.intermine.app.core.templates.constraint.PathConstraint;
import org.intermine.app.core.templates.constraint.PathConstraintAttribute;
import org.intermine.app.core.templates.constraint.PathConstraintSimpleMultiValue;
import org.intermine.app.util.Strs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class MultiValueConstraintView extends ConstraintView {
    @InjectView(R.id.operations_spinner)
    protected Spinner mOperationsSpinner;

    @InjectView(R.id.constraint_path)
    protected TextView mPath;

    @InjectView(R.id.values)
    protected RecyclerView mValues;

    private MultiValueListAdapter mAdapter;
    private PathConstraintSimpleMultiValue mConstraint;

    public MultiValueConstraintView(Context context, PathConstraintSimpleMultiValue constraint) {
        super(context, constraint);
        mConstraint = constraint;

        init();
    }

    public MultiValueConstraintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiValueConstraintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public boolean isValidConstraint() {
        //return !Strs.isNullOrEmpty(mValue.getText().toString());
        return true;
    }

    @Override
    public void highlightInvalid() {
        //mValue.setError(getContext().getString(R.string.empty_value_em));
    }

    private void init() {
        inflate(getContext(), R.layout.multivalue_constraint_view, this);
        ButterKnife.inject(this);

        SimpleAdapter<ConstraintOperation> adapter = new SimpleAdapter(getContext());
        List<ConstraintOperation> data = Arrays.asList(ConstraintOperation.ONE_OF, ConstraintOperation.NONE_OF);
        adapter.updateData(data);
        mOperationsSpinner.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mValues.setLayoutManager(manager);
        mAdapter = new MultiValueListAdapter(mConstraint.getValues());
        mValues.setAdapter(mAdapter);
        mValues.setItemAnimator(new DefaultItemAnimator());

        if (null != mConstraint) {
            //mValue.setText(mConstraint.getValue());
            mPath.setText(mConstraint.getPath());

            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).equals(mConstraint.getOperation())) {
                    mOperationsSpinner.setSelection(i);
                }
            }
        }
    }

    public String getValues() {
        return null;
    }

    public ConstraintOperation getOperation() {
        return (ConstraintOperation) mOperationsSpinner.getSelectedItem();
    }

    @Override
    public PathConstraint getGeneratedConstraint() {
        PathConstraintAttribute constraint = (PathConstraintAttribute) getPathConstraint();
        return new PathConstraintAttribute(constraint.getPath(), getOperation(), getValues(),
                constraint.getCode());
    }
}
