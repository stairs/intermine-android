package org.intermine.app.net.request.get;

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

import org.intermine.app.R;
import org.intermine.app.core.templates.TemplateParameter;
import org.intermine.app.net.request.JsonGetAuthRequest;
import org.intermine.app.util.Collections;

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
    public Map<String, String> getUrlParams() {
        Map<String, String> params = super.getUrlParams();
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