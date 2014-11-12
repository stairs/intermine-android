package org.intermine.net.request.get;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.lang3.ArrayUtils;
import org.intermine.R;
import org.intermine.core.templates.Template;
import org.intermine.template.TemplateQuery;
import org.intermine.util.Collections;
import org.springframework.http.MediaType;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.intermine.net.request.get.GetTemplatesRequest.Templates;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetTemplatesRequest extends JsonGetRequest<Templates> {

    public GetTemplatesRequest(Context ctx) {
        super(Templates.class, ctx, null, null);

        setOutWrapper("templates");
    }

    @Override
    public Map<String, ?> getUrlParams() {
        Map<String, String> params = Collections.newHashMap();
        params.put(FORMAT_PARAM, "json");
        return params;
    }

    @Override
    public String getUrl() {
        Context ctx = getContext();
        return ctx.getString(R.string.flymine_url) + ctx.getString(R.string.templates_path);
    }

    public static class Templates extends HashMap<String, Template> {

    }
}