package org.intermine.net.request.get;

import android.content.Context;

import org.intermine.R;
import org.intermine.core.templates.Template;
import org.intermine.net.request.JsonGetRequest;
import org.intermine.util.Collections;

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