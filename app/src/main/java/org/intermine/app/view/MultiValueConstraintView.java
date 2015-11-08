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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.intermine.app.R;
import org.intermine.app.adapter.MultiValueListAdapter;
import org.intermine.app.adapter.SimpleAdapter;
import org.intermine.app.core.templates.constraint.ConstraintOperation;
import org.intermine.app.core.templates.constraint.PathConstraint;
import org.intermine.app.core.templates.constraint.PathConstraintMultiValue;
import org.intermine.app.core.templates.constraint.PathConstraintSimpleMultiValue;

import java.util.Arrays;
import java.util.Collection;
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
        return !mAdapter.getSelected().isEmpty();
    }

    @Override
    public void highlightInvalid() {
        Toast.makeText(getContext(), "Please, select at least one value from the list!", Toast.LENGTH_SHORT).show();
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
            mPath.setText(mConstraint.getPath());

            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).equals(mConstraint.getOperation())) {
                    mOperationsSpinner.setSelection(i);
                }
            }
        }
    }

    public Collection<String> getValues() {
        return mAdapter.getSelected();
    }

    public ConstraintOperation getOperation() {
        return (ConstraintOperation) mOperationsSpinner.getSelectedItem();
    }

    @Override
    public PathConstraint getGeneratedConstraint() {
        PathConstraint constraint = getPathConstraint();
        return new PathConstraintMultiValue(constraint.getPath(), getOperation(), getValues(),
                constraint.getCode());
    }
}
