package org.intermine.app.net.request.post;

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

import com.google.gson.Gson;

import org.intermine.app.R;
import org.intermine.app.core.ListItems;
import org.intermine.app.net.NoRetryPolicy;
import org.intermine.app.net.request.PostAuthRequest;
import org.intermine.app.util.Strs;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class FetchListResultsRequest extends PostAuthRequest<ListItems> {
    public static final String FORMAT_PARAM = "format";
    public static final String JSON = "json";
    public static final String QUERY_PARAM = "query";
    public static final String START_PARAM = "start";
    public static final String SIZE_PARAM = "size";


    private int mStart;
    private int mSize;
    private String mListName;
    private String mQuery;

    public FetchListResultsRequest(Context ctx, String mineName, String listType, String listName, int start, int size) {
        super(ListItems.class, ctx, null, null, null, mineName);
        mListName = listName;

        String template = ctx.getString(R.string.list_query);
        Map<String, List<String>> typeFieldsMap = getStorage().getTypeFields(mineName);
        List<String> typeFields = typeFieldsMap.get(listType);

        String columns = Strs.EMPTY_STRING;
        if (null != typeFields){
            columns = Strs.join(typeFields, " ");
        }
        mQuery = String.format(template, columns, listType, mListName);
        mStart = start;
        mSize = size;

        setRetryPolicy(new NoRetryPolicy());
    }

    @Override
    public String getUrl() {
        return getBaseUrl(mMineName) + getContext().getString(R.string.search_path);
    }

    @Override
    public MultiValueMap<String, String> getPost() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(FORMAT_PARAM, JSON);
        params.add(QUERY_PARAM, mQuery);

        if (mSize > 0) {
            params.add(START_PARAM, Integer.toString(mStart));
            params.add(SIZE_PARAM, Integer.toString(mSize));
        }
        return params;
    }

    @Override
    public ListItems loadDataFromNetwork() throws Exception {
        String json = post();

        Gson mapper = getMapper();
        return mapper.fromJson(json, ListItems.class);
    }

    protected Gson getMapper() {
        return new Gson();
    }
}
