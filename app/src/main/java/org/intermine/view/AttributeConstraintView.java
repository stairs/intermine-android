package org.intermine.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import org.intermine.R;
import org.intermine.adapter.SimpleAdapter;
import org.intermine.core.templates.constraint.ConstraintOperation;
import org.intermine.core.templates.constraint.PathConstraintAttribute;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class AttributeConstraintView extends RelativeLayout {
    @InjectView(R.id.operations_spinner)
    Spinner mOperationsSpinner;

    @InjectView(R.id.base_constraint_value)
    EditText mValue;

    public AttributeConstraintView(Context context, String value) {
        super(context);

        init();
        mValue.setText(value);
    }

    public AttributeConstraintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AttributeConstraintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.attribute_constraint_view, this);
        ButterKnife.inject(this);

        SimpleAdapter<ConstraintOperation> adapter = new SimpleAdapter(getContext());
        adapter.updateData(PathConstraintAttribute.VALID_OPS);
        mOperationsSpinner.setAdapter(adapter);
    }

    public String getValue() {
        return mValue.getText().toString();
    }

    public ConstraintOperation getOperation() {
        return (ConstraintOperation) mOperationsSpinner.getSelectedItem();
    }
}
