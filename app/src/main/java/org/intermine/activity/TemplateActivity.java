package org.intermine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.intermine.R;
import org.intermine.core.templates.TemplateParameter;
import org.intermine.core.templates.constraint.PathConstraint;
import org.intermine.core.templates.constraint.PathConstraintLookup;
import org.intermine.core.templates.Template;
import org.intermine.core.templates.constraint.PathConstraintAttribute;
import org.intermine.util.Collections;
import org.intermine.util.Templates;
import org.intermine.view.AttributeConstraintView;
import org.intermine.view.ConstraintView;
import org.intermine.view.LookupConstraintView;

import java.util.ArrayList;
import java.util.Collection;
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

            Spanned descriptionText = Html.fromHtml(mTemplate.getDescription());
            mTemplateDescription.setText(descriptionText);

            List<PathConstraint> pathConstraints = Templates.convertToPathConstraints(
                    mTemplate.getConstraints(), getStorage().getMineModel(mMineName));
            Collection<View> views = generateViewsForConstraints(pathConstraints);

            for (View view : views) {
                mContainer.addView(view);
            }
        }
    }

    // --------------------------------------------------------------------------------------------
    // Callbacks
    // --------------------------------------------------------------------------------------------

    @OnClick(R.id.show_results)
    protected void showTemplatesResults() {
        ArrayList<TemplateParameter> parameters = Collections.newArrayList();

        for (int i = 0; i < mContainer.getChildCount(); i++) {
            ConstraintView constraintView = (ConstraintView) mContainer.getChildAt(i);
            PathConstraint pathConstraint = constraintView.getGeneratedConstraint();
            parameters.add(generateTemplateParameter(pathConstraint));
        }

        TemplateResultsActivity.start(this, mTemplate.getName(), mMineName, parameters);
        finish();
    }

    // --------------------------------------------------------------------------------------------
    // Helper Methods
    // --------------------------------------------------------------------------------------------

    protected Collection<View> generateViewsForConstraints(List<PathConstraint> pathConstraints) {
        Collection<View> views = Collections.newArrayList();

        for (PathConstraint constraint : pathConstraints) {
            View view = generateViewForConstraints(constraint);

            if (null != view) {
                views.add(view);
            }
        }
        return views;
    }

    protected View generateViewForConstraints(PathConstraint pathConstraint) {
        View view = null;

        if (pathConstraint instanceof PathConstraintLookup) {
            view = new LookupConstraintView(this, (PathConstraintLookup) pathConstraint);
        } else if (pathConstraint instanceof PathConstraintAttribute) {
            view = new AttributeConstraintView(this, (PathConstraintAttribute) pathConstraint);
        }
        return view;
    }

    private TemplateParameter generateTemplateParameter(PathConstraint constraint) {
        String path = constraint.getPath();
        String operation = constraint.getOperation().toString();
        String code = constraint.getCode();

        if (!Collections.isNullOrEmpty(PathConstraint.getValues(constraint))) {
            return new TemplateParameter(path, operation,
                    (List<String>) PathConstraint.getValues(constraint), code);
        } else {
            return new TemplateParameter(path, operation,
                    PathConstraint.getValue(constraint),
                    PathConstraint.getExtraValue(constraint), code);
        }
    }
}