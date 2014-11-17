package org.intermine.net.request.get;

import android.content.Context;

import org.intermine.R;
import org.intermine.core.List;
import org.intermine.net.request.JsonGetAuthRequest;
import org.intermine.net.request.JsonGetRequest;
import org.intermine.util.Collections;
import org.intermine.util.Strs;

import java.util.ArrayList;
import java.util.Map;

import static org.intermine.net.request.get.GetListsRequest.Lists;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetListsRequest extends JsonGetAuthRequest<Lists> {
    private static final String LIST_NAME_PARAM = "name";
    private String mListName;

    public GetListsRequest(Context ctx, String mineName, String listName) {
        super(Lists.class, ctx, null, null, mineName);
        mListName = listName;
        setOutWrapper("lists");
    }

    @Override
    public Map<String, ?> getUrlParams() {
        Map<String, String> params = Collections.newHashMap();
        params.put(FORMAT_PARAM, "json");
        params.put(LIST_NAME_PARAM, mListName);
        return params;
    }

    @Override
    public String getUrl() {
        return getBaseUrl() + getContext().getString(R.string.lists_path);
    }

    public static class Lists extends ArrayList<List> {

    }
}
