package org.intermine.net.request.post;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.octo.android.robospice.retry.RetryPolicy;

import org.intermine.R;
import org.intermine.core.ListItems;
import org.intermine.net.NoRetryPolicy;
import org.intermine.util.Collections;
import org.intermine.util.Strs;
import org.intermine.util.Uris;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpMethod.POST;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class PostListResultsRequest extends PostRequest<ListItems> {
    public static final String FORMAT_PARAM = "format";
    public static final String JSON = "json";
    public static final String QUERY_PARAM = "query";
    public static final String START_PARAM = "start";
    public static final String SIZE_PARAM = "size";

    private int mStart;
    private int mSize;
    private String mListName;
    private String mQuery;

    public PostListResultsRequest(Context ctx, String listName, int start, int size) {
        super(ListItems.class, ctx, null, null, null);
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

        if (mSize > 0){
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
