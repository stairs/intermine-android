package org.intermine.net.request.get;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public abstract class GetQueryResultsRequest<T> extends JsonGetRequest<T> {
    public static final String JSON_FORMAT = "json";
    public static final int DEFAULT_RESULTS_SIZE = 15;

    private static final String QUERY_PARAM = "query";
    private static final String START_PARAM = "start";
    private static final String SIZE_PARAM = "size";
    private static final String FORMAT_PARAM = "format";

    private String mTemplate;
    private int mStart;
    private String mFormat;
    private int mSize;

    public GetQueryResultsRequest(Class clazz, Context ctx, String url, Map<String, ?> params) {
        super(clazz, ctx, url, params);
        setUrl(url + "/results?query={query}&format={format}&size={size}&start={start}&token=T1M4e9V2b2Mdm3S87eM9");
        mFormat = JSON_FORMAT;
        mSize = DEFAULT_RESULTS_SIZE;
        mStart = 0;
        setOutWrapper("results");
    }

    @Override
    public Map<String, ?> getUrlParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(QUERY_PARAM, generateQuery(mTemplate));
        params.put(FORMAT_PARAM, mFormat);
        params.put(SIZE_PARAM, String.valueOf(mSize));
        params.put(START_PARAM, String.valueOf(mStart));
        return params;
    }

    abstract protected String generateQuery(String template);

    public String getTemplate() {
        return mTemplate;
    }

    public void setTemplate(String template) {
        mTemplate = template;
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
