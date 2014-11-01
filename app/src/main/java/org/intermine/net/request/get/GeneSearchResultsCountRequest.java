package org.intermine.net.request.get;

import android.content.Context;

import org.intermine.R;
import org.intermine.core.ResultsCount;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GeneSearchResultsCountRequest extends JsonGetRequest<ResultsCount> {
    public static final String COUNT_FORMAT = "jsoncount";
    public static final String JSON_FORMAT = "json";
    public static final int DEFAULT_SIZE = 10;

    private static final String QUERY_PARAM = "query";
    private static final String START_PARAM = "start";
    private static final String SIZE_PARAM = "size";
    private static final String FORMAT_PARAM = "format";
    private static final String FACET_CATEGORY_PARAM = "facet_Category";
    private static final String FACET_CATEGORY_VALUE = "Gene";

    public static final String GENE_NAME_SEARCH_QUERY = "<query name=\"\" model=\"genomic\" view=\"Gene.secondaryIdentifier " +
            "Gene.symbol Gene.locations.start Gene.locations.end Gene.locations.strand Gene.locations.locatedOn.primaryIdentifier " +
            "Gene.description Gene.name\" constraintLogic=\"A or B\">" +
            "<constraint path=\"Gene.secondaryIdentifier\" op=\"=\" value=\"%1$s*\"/>" +
            "<constraint path=\"Gene.symbol\" op=\"=\" value=\"%2$s*\"/>" +
            "</query>";

    private String mQuery;
    private int mStart;
    private String mFormat;
    private int mSize;

    public GeneSearchResultsCountRequest(Class clazz, Context ctx, String url, Map<String, ?> params) {
        super(clazz, ctx, url, params);
    }

    public GeneSearchResultsCountRequest(Context ctx, String q) {
        this(ResultsCount.class, ctx, null, null);
        mQuery = q;
        mFormat = JSON_FORMAT;
        mSize = DEFAULT_SIZE;
        mStart = 0;
    }

    public GeneSearchResultsCountRequest(Context ctx, String q, String format, int start) {
        this(ctx, q);
        mFormat = format;
        mStart = start;
    }

    @Override
    public String getUrl() {
        return getContext().getString(R.string.yeastmine_url) +
                getContext().getString(R.string.search_path) +
                "?query={query}&format={format}&size={size}&start={start}";
    }

    @Override
    public Map<String, ?> getUrlParams() {
        Map<String, String> params = new HashMap<String, String>();
        String query = String.format(GENE_NAME_SEARCH_QUERY, mQuery, mQuery);
        params.put(QUERY_PARAM, query);
        params.put(FORMAT_PARAM, mFormat);
        params.put(SIZE_PARAM, String.valueOf(mSize));
        params.put(START_PARAM, String.valueOf(mStart));
        return params;
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
