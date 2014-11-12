package org.intermine.net.request.get;

import android.content.Context;
import android.util.Log;

import org.intermine.R;
import org.intermine.core.ListItems;
import org.intermine.core.templates.Condition;
import org.intermine.core.templates.Template;
import org.intermine.core.templates.TemplateParameter;
import org.intermine.pathquery.PathConstraint;
import org.intermine.pathquery.PathQuery;
import org.intermine.template.SwitchOffAbility;
import org.intermine.template.TemplateQuery;
import org.intermine.util.Collections;
import org.intermine.metadata.Model;
import org.intermine.util.Uris;
import org.intermine.webservice.client.services.ModelService;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.intermine.net.request.get.GetTemplatesRequest.Templates;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetTemplateResultsRequest extends JsonGetRequest<ListItems> {
    private Template mTemplate;
    private ModelService ms;

    public GetTemplateResultsRequest(Context ctx, Template template) {
        super(ListItems.class, ctx, null, null);
        mTemplate = template;
    }

    @Override
    public Map<String, ?> getUrlParams() {
        Map<String, String> params = Collections.newHashMap();
        params.put(FORMAT_PARAM, "json");
        params.put("name", mTemplate.getName());
        params.put("start", "0");
        params.put("size", "10");

        //TemplateQuery query = convert2TemplateQuery(mTemplate);
        setTemplateParameters(getParametersFor(mTemplate), params);

        return params;
    }

    @Override
    public String getUrl() {
        Context ctx = getContext();
        return ctx.getString(R.string.flymine_url) + ctx.getString(R.string.template_results_path);
    }

    private List<TemplateParameter> getParametersFor(TemplateQuery template) {
        List<TemplateParameter> params = new ArrayList<TemplateParameter>();

        for (PathConstraint pc : template.getEditableConstraints()) {
            if (template.getSwitchOffAbility(pc) != SwitchOffAbility.OFF) {
                TemplateParameter tp;
                String path = pc.getPath();
                String op = pc.getOp().toString();

                String code = template.getConstraints().get(pc);
                if (PathConstraint.getValues(pc) != null) {
                    tp = new TemplateParameter(path, op, PathConstraint.getValues(pc), code);
                } else {
                    tp = new TemplateParameter(path, op, PathConstraint.getValue(pc), PathConstraint.getExtraValue(pc), code);
                }
                params.add(tp);
            }
        }
        return params;
    }

    private List<TemplateParameter> getParametersFor(Template template) {
        List<TemplateParameter> params = new ArrayList<>();

        for (Condition condition : template.getConditions()) {
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


    protected String expandQuery(String uriString, Map<String, ?> params) {
        return Uris.expandQuery(uriString, params);
    }

//    protected TemplateQuery convert2TemplateQuery(Template template) {
//        ModelService modelService = getModelService();
//        Model model = null;
//
//        try {
//            model = modelService.getModel();
//        } catch (Exception ex) {
//            Log.e("ddd", ex.toString());
//        }
//
//        String name = template.getModel().getName();
//        model = Model.getInstanceByName(name);
//        PathQuery pathQuery = new PathQuery(model);
//        pathQuery.
//
//                TemplateQuery templateQuery = new TemplateQuery(template.getName(),
//                template.getTitle(), template.getComment(), pathQuery);
//        return templateQuery;
//    }

    /**
     * Return a new ModelService for retrieving the data model.
     *
     * @return model service
     */
    public ModelService getModelService() {
        if (ms == null) {
            ms = new ModelService(getContext().getString(R.string.flymine_url), "InterMine-WS-Client-Java-2.0");
        }
        return ms;
    }
}