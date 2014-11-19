package org.intermine.net.request.get;

import android.content.Context;

import org.intermine.R;
import org.intermine.core.templates.Template;
import org.intermine.core.templates.TemplateParameter;
import org.intermine.core.templates.constraint.Constraint;
import org.intermine.core.templates.constraint.SwitchOffAbility;
import org.intermine.net.request.JsonGetAuthRequest;
import org.intermine.util.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetTemplateResultsRequest<T> extends JsonGetAuthRequest<T> {
    public static final String JSON_FORMAT = "json";
    public static final String COUNT_FORMAT = "count";

    private static final String CONSTRAINT_PARAM = "constraint";
    private static final String OP_PARAM = "op";
    private static final String VALUE_PARAM = "value";
    private static final String EXTRA_PARAM = "extra";
    private static final String CODE_PARAM = "code";

    private static final String NAME_PARAM = "name";
    private static final String START_PARAM = "start";
    private static final String SIZE_PARAM = "size";

    private String mTemplateName;
    private ArrayList<TemplateParameter> mTemplateParams;
    private int mSize;
    private int mStart;
    private String mFormat;

    public GetTemplateResultsRequest(Class clazz, Context ctx, String templateName,
                                     ArrayList<TemplateParameter> templateParams,
                                     String mineName, int start, int size) {
        super(clazz, ctx, null, null, mineName);
        mTemplateName = templateName;
        mTemplateParams = templateParams;
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
        params.put(NAME_PARAM, mTemplateName);
        params.put(START_PARAM, Integer.toString(mStart));
        params.put(SIZE_PARAM, Integer.toString(mSize));
        params.putAll(generateUrlParams(mTemplateParams));
        return params;
    }

    @Override
    public String getUrl() {
        Context ctx = getContext();
        return getBaseUrl() + ctx.getString(R.string.template_results_path);
    }

    private Map<String, String> generateUrlParams(List<TemplateParameter> parameters) {
        Map<String, String> params = Collections.newHashMap();

        for (int i = 0; i < parameters.size(); i++) {
            int index = i + 1;
            TemplateParameter param = parameters.get(i);

            params.put(CONSTRAINT_PARAM + index, param.getPathId());
            params.put(OP_PARAM + index, param.getOperation());

            String valueIndex = VALUE_PARAM + index;
            if (param.isMultiValue()) {
                for (String value : param.getValues()) {
                    params.put(valueIndex, value);
                }
            } else {
                params.put(valueIndex, param.getValue());
            }
            if (param.getExtraValue() != null) {
                params.put(EXTRA_PARAM + index, param.getExtraValue());
            }
            if (param.getCode() != null) {
                params.put(CODE_PARAM + index, param.getCode());
            }
        }
        return params;
    }
}