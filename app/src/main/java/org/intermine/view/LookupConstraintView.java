package org.intermine.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.intermine.R;
import org.intermine.core.templates.constraint.ConstraintOperation;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class LookupConstraintView extends RelativeLayout implements ConstraintView {
    @InjectView(R.id.lookup_value)
    EditText mValue;

    public LookupConstraintView(Context context, String value) {
        super(context);

        init();
        mValue.setText(value);
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
        ButterKnife.inject(this);
    }

    public String getValue() {
        return mValue.getText().toString();
    }

    @Override
    public String getOperation() {
        return ConstraintOperation.LOOKUP.toString();
    }
}
