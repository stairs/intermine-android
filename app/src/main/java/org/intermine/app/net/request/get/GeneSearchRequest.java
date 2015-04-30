package org.intermine.app.net.request.get;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.intermine.app.core.Gene;
import org.intermine.app.core.GenesList;
import org.intermine.app.json.GeneSearchResultDeserializer;
import org.intermine.app.net.request.JsonGetRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GeneSearchRequest extends JsonGetRequest<GenesList> {
    public static final String JSON_FORMAT = "json";
    public static final int DEFAULT_SIZE = 10;

    private static final String QUERY_PARAM = "q";
    private static final String START_PARAM = "start";
    private static final String SIZE_PARAM = "size";
    private static final String FORMAT_PARAM = "format";
    private static final String FACET_CATEGORY_PARAM = "facet_Category";
    private static final String FACET_CATEGORY_VALUE = "Gene";

    private String mQuery;
    private int mStart;
    private String mFormat;
    private int mSize;
    private String mMineName;

    public GeneSearchRequest(Class clazz, Context ctx, String url, Map<String, ?> params) {
        super(clazz, ctx, url, params);
    }

    public GeneSearchRequest(Context ctx, String q, String mineName) {
        this(GenesList.class, ctx, null, null);
        mQuery = q;
        mFormat = JSON_FORMAT;
        mSize = DEFAULT_SIZE;
        mStart = 0;
        mMineName = mineName;
    }

    @Override
    public Gson getMapper() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(GenesList.class, new GeneSearchResultDeserializer());
        return builder.create();
    }

    public GeneSearchRequest(Context ctx, String q,
                             String mineName, String format, int start) {
        this(ctx, q, mineName);
        mFormat = format;
        mStart = start;
    }

    @Override
    public String getUrl() {
        return getBaseUrl(mMineName) + "/search";
    }

    @Override
    public Map<String, ?> getUrlParams() {
        Map<String, String> params = new HashMap<>();
        params.put(QUERY_PARAM, mQuery);
        params.put(FACET_CATEGORY_PARAM, FACET_CATEGORY_VALUE);
        params.put(FORMAT_PARAM, mFormat);
        params.put(SIZE_PARAM, String.valueOf(mSize));
        params.put(START_PARAM, String.valueOf(mStart));
        return params;
    }

    @Override
    public GenesList loadDataFromNetwork() throws Exception {
        GenesList result = super.loadDataFromNetwork();

        if (null != result && !result.isEmpty()) {
            for (Gene gene : result) {
                gene.setMine(mMineName);
            }
        }
        return result;
    }

    public String getQuery() {
        return mQuery;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    public int getStart() {
        return mStart;
    }

    public void setStart(int start) {
        mStart = start;
    }

    public String getFormat() {
        return mFormat;
    }

    public void setFormat(String format) {
        mFormat = format;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }
}
