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
import org.intermine.app.core.List;
import org.intermine.app.net.request.JsonGetAuthRequest;

import java.util.ArrayList;
import java.util.Map;

import static org.intermine.app.net.request.get.GetListsRequest.Lists;

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
    public Map<String, String> getUrlParams() {
        Map<String, String> params = super.getUrlParams();
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
