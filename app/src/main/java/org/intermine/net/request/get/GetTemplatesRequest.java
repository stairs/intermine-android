package org.intermine.net.request.get;

import android.content.Context;

import org.intermine.R;
import org.intermine.core.templates.Template;
import org.intermine.net.request.JsonGetAuthRequest;

import java.util.HashMap;
import java.util.Map;

import static org.intermine.net.request.get.GetTemplatesRequest.Templates;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetTemplatesRequest extends JsonGetAuthRequest<Templates> {

    public GetTemplatesRequest(Context ctx, String mineName) {
        super(Templates.class, ctx, null, null, mineName);

        setOutWrapper("templates");
    }

    @Override
    public Map<String, String> getUrlParams() {
        Map<String, String> params = super.getUrlParams();
        params.put(FORMAT_PARAM, "json");
        return params;
    }

    @Override
    public String getUrl() {
        Context ctx = getContext();
        return getBaseUrl() + ctx.getString(R.string.templates_path);
    }

    public static class Templates extends HashMap<String, Template> {

    }
}