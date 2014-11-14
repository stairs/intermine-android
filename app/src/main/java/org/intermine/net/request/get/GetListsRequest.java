package org.intermine.net.request.get;

import android.content.Context;

import org.intermine.R;
import org.intermine.core.List;
import org.intermine.net.request.JsonGetRequest;
import org.intermine.util.Collections;

import java.util.ArrayList;
import java.util.Map;

import static org.intermine.net.request.get.GetListsRequest.Lists;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetListsRequest extends JsonGetRequest<Lists> {

    public GetListsRequest(Context ctx) {
        super(Lists.class, ctx, null, null);

        setOutWrapper("lists");
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
        return ctx.getString(R.string.flymine_url) + ctx.getString(R.string.lists_path);
    }

    public static class Lists extends ArrayList<List> {

    }
}
