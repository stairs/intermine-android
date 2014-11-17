package org.intermine.net.request.get;

import android.content.Context;

import org.intermine.R;
import org.intermine.core.ListItems;
import org.intermine.core.templates.Template;
import org.intermine.core.templates.TemplateParameter;
import org.intermine.core.templates.constraint.Constraint;
import org.intermine.net.request.JsonGetAuthRequest;
import org.intermine.net.request.JsonGetRequest;
import org.intermine.util.Collections;
import org.intermine.util.Uris;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetTemplateResultsRequest<T> extends JsonGetAuthRequest<T> {
    public static final String JSON_FORMAT = "json";
    public static final String COUNT_FORMAT = "count";

    private static final String NAME_PARAM = "name";
    private static final String START_PARAM = "start";
    private static final String SIZE_PARAM = "size";

    private Template mTemplate;
    private int mSize;
    private int mStart;
    private String mFormat;

    public GetTemplateResultsRequest(Class clazz, Context ctx, Template template,
                                     String mineName, int start, int size) {
        super(clazz, ctx, null, null, mineName);
        mTemplate = template;
        mSize = size;
        mStart = start;

        if (clazz.equals(Integer.class)) {
            mFormat = COUNT_FORMAT;
        } else {
            mFormat = JSON_FORMAT;
        }
    }

    @Override
    public Map<String, ?> getUrlParams() {
        Map<String, String> params = Collections.newHashMap();
        params.put(FORMAT_PARAM, mFormat);
        params.put(NAME_PARAM, mTemplate.getName());
        params.put(START_PARAM, Integer.toString(mStart));
        params.put(SIZE_PARAM, Integer.toString(mSize));

        //TemplateQuery query = convert2TemplateQuery(mTemplate);
        setTemplateParameters(getParametersFor(mTemplate), params);
        return params;
    }

    @Override
    public String getUrl() {
        Context ctx = getContext();
        return getBaseUrl() + ctx.getString(R.string.template_results_path);
    }

    private List<TemplateParameter> getParametersFor(Template template) {
        List<TemplateParameter> params = new ArrayList<>();

        for (Constraint condition : template.getConstraints()) {
            if (!condition.getSwitched().equals(org.intermine.core.templates.SwitchOffAbility.OFF.name())) {
                TemplateParameter tp;
                String path = condition.getPath();
                String op = condition.getOp().toString();

                String code = condition.getCode();
//                if (PathConstraint.getValues(pc) != null) {
//                    tp = new TemplateParameter(path, op, PathConstraint.getValues(pc), code);
//                } else {
                tp = new TemplateParameter(path, op, condition.getValue(), null, code);
//                }
                params.add(tp);
            }
        }
        return params;
    }

    private void setTemplateParameters(List<TemplateParameter> parameters, Map<String, String> params) {
        for (int i = 0; i < parameters.size(); i++) {
            TemplateParameter par = parameters.get(i);
            int index = i + 1;
            params.put("constraint" + index, par.getPathId());
            params.put("op" + index, "=");
            String valueIndex = "value" + index;
            if (par.isMultiValue()) {
                for (String value : par.getValues()) {
                    params.put(valueIndex, value);
                }
            } else {
                params.put(valueIndex, par.getValue());
            }
            if (par.getExtraValue() != null) {
                params.put("extra" + index, par.getExtraValue());
            }
            if (par.getCode() != null) {
                params.put("code" + index, par.getCode());
            }
        }
    }
}