package org.intermine.app.activity;

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
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.intermine.app.R;
import org.intermine.app.core.templates.Template;
import org.intermine.app.core.templates.TemplateParameter;
import org.intermine.app.core.templates.constraint.PathConstraint;
import org.intermine.app.core.templates.constraint.PathConstraintAttribute;
import org.intermine.app.core.templates.constraint.PathConstraintLookup;
import org.intermine.app.core.templates.constraint.PathConstraintSimpleMultiValue;
import org.intermine.app.util.Collections;
import org.intermine.app.util.Strs;
import org.intermine.app.util.Templates;
import org.intermine.app.util.Views;
import org.intermine.app.view.AttributeConstraintView;
import org.intermine.app.view.ConstraintView;
import org.intermine.app.view.LookupConstraintView;
import org.intermine.app.view.MultiValueConstraintView;

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

    @InjectView(R.id.description_card)
    View mDescriptionContainer;

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
    // Lifecycle
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

            if (Strs.isNullOrEmpty(mTemplate.getDescription())) {
                Views.setGone(mDescriptionContainer);
            } else {
                Views.setVisible(mDescriptionContainer);
                Spanned descriptionText = Html.fromHtml(Strs.nullToEmpty(mTemplate.getDescription()));
                mTemplateDescription.setText(descriptionText);
            }

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
        if (checkAllConstraintsValid()) {
            ArrayList<TemplateParameter> parameters = Collections.newArrayList();

            for (int i = 0; i < mContainer.getChildCount(); i++) {
                ConstraintView constraintView = (ConstraintView) mContainer.getChildAt(i);
                PathConstraint pathConstraint = constraintView.getGeneratedConstraint();
                parameters.add(generateTemplateParameter(pathConstraint));
            }

            TemplateResultsActivity.start(this, mTemplate.getName(), mTemplate.getTitle(),
                    mMineName, parameters);
            finish();
        }
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
        } else if (pathConstraint instanceof PathConstraintSimpleMultiValue) {
            view = new MultiValueConstraintView(this, (PathConstraintSimpleMultiValue) pathConstraint);
        }
        return view;
    }

    private boolean checkAllConstraintsValid() {
        boolean allValid = true;

        for (int i = 0; i < mContainer.getChildCount(); i++) {
            ConstraintView constraintView = (ConstraintView) mContainer.getChildAt(i);

            if (!constraintView.isValidConstraint()) {
                allValid = false;
                constraintView.highlightInvalid();
            }
        }
        return allValid;
    }

    private TemplateParameter generateTemplateParameter(PathConstraint constraint) {
        String path = constraint.getPath();
        String operation = constraint.getOperation().toString();
        String code = constraint.getCode();

        if (!Collections.isNullOrEmpty(PathConstraint.getValues(constraint))) {
            return new TemplateParameter(path, operation, PathConstraint.getValues(constraint), code);
        } else {
            return new TemplateParameter(path, operation,
                    PathConstraint.getValue(constraint),
                    PathConstraint.getExtraValue(constraint), code);
        }
    }
}