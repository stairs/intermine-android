package org.intermine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.intermine.R;
import org.intermine.core.model.Model;
import org.intermine.core.templates.SwitchOffAbility;
import org.intermine.core.templates.Template;
import org.intermine.core.templates.constraint.Constraint;
import org.intermine.core.templates.constraint.ConstraintOperation;
import org.intermine.core.templates.constraint.PathConstraintAttribute;
import org.intermine.util.Collections;
import org.intermine.view.AttributeConstraintView;
import org.intermine.view.ConstraintView;
import org.intermine.view.LookupConstraintView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class TemplateActivity extends BaseActivity {
    public static final String TEMPLATE_KEY = "template_key";
    public static final String MINE_NAME_KEY = "mine_name_key";

    @InjectView(R.id.constraints_container)
    ViewGroup mContainer;

    @InjectView(R.id.template_description)
    TextView mTemplateDescription;

    private Template mTemplate;
    private String mMineName;

    // --------------------------------------------------------------------------------------------
    // Static Methods
    // --------------------------------------------------------------------------------------------

    public static void start(Context context, Template template, String mineName) {
        Intent intent = new Intent(context, TemplateActivity.class);
        intent.putExtra(TEMPLATE_KEY, template);
        intent.putExtra(MINE_NAME_KEY, mineName);
        context.startActivity(intent);
    }

    // --------------------------------------------------------------------------------------------
    // Fragment Lifecycle
    // --------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity);
        ButterKnife.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTemplate = getIntent().getParcelableExtra(TEMPLATE_KEY);
        mMineName = getIntent().getStringExtra(MINE_NAME_KEY);

        if (null != mTemplate) {
            setTitle(mTemplate.getTitle());
            mTemplateDescription.setText(mTemplate.getDescription());

            processConstraints(mTemplate.getConstraints());
        }
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    @OnClick(R.id.show_results)
    protected void showTemplatesResults() {
        List<Constraint> queryConstraints = Collections.newArrayList();

        for (int i = 0; i < mContainer.getChildCount(); i++) {
            View view = mContainer.getChildAt(i);
            ConstraintView constraintView = (ConstraintView) view;
            Constraint constraint = (Constraint) view.getTag();
            constraint.setOp(constraintView.getValue());
            constraint.setValue(constraintView.getValue());
            queryConstraints.add(constraint);
        }
        mTemplate.setConstraints(queryConstraints);

        TemplateResultsActivity.start(this, mTemplate, mMineName);
        finish();
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected void processConstraints(List<Constraint> constraints) {
        Model model = getStorage().getMineModel(mMineName);

        for (Constraint constraint : constraints) {
            if (!SwitchOffAbility.OFF.toString().equals(constraint.getSwitched())) {
                ConstraintOperation operation = ConstraintOperation.valueByName(constraint.getOp());

                View view = null;

                if (ConstraintOperation.LOOKUP.equals(operation)) {
                    view = new LookupConstraintView(this, constraint.getValue());
                    mContainer.addView(view);
                } else if (PathConstraintAttribute.VALID_OPERATIONS.contains(operation)) {
                    view = new AttributeConstraintView(this, constraint.getValue());
                    mContainer.addView(view);
                }

                if (null != view) {
                    view.setTag(constraint);
                }
            }
        }
    }
}
