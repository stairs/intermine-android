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
import org.intermine.app.core.templates.Template;
import org.intermine.app.net.request.JsonGetAuthRequest;

import java.util.HashMap;
import java.util.Map;

import static org.intermine.app.net.request.get.GetTemplatesRequest.Templates;

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