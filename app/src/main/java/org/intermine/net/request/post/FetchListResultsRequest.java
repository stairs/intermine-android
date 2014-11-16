package org.intermine.net.request.post;

import android.content.Context;

import com.google.gson.Gson;

import org.intermine.R;
import org.intermine.core.ListItems;
import org.intermine.net.NoRetryPolicy;
import org.intermine.net.request.PostAuthRequest;
import org.intermine.net.request.PostRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

    public FetchListResultsRequest(Context ctx, String mineName, String listName, int start, int size) {
        super(ListItems.class, ctx, null, null, null, mineName);
        mListName = listName;

        String template = ctx.getString(R.string.list_query);
        mQuery = String.format(template, mListName);

        mStart = start;
        mSize = size;

        setRetryPolicy(new NoRetryPolicy());
    }

    @Override
    public String getUrl() {
        Context ctx = getContext();
        return ctx.getString(R.string.flymine_url) + ctx.getString(R.string.search_path);
    }

    @Override
    public MultiValueMap<String, String> getPost() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
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
        ListItems listItems = mapper.fromJson(json, ListItems.class);
        return listItems;
    }

    protected Gson getMapper() {
        return new Gson();
    }
}
