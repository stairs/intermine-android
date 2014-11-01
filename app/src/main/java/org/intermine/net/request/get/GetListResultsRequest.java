package org.intermine.net.request.get;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.ArrayUtils;
import org.intermine.R;
import org.intermine.core.ListItems;
import org.intermine.util.Strs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetListResultsRequest extends GetQueryResultsRequest<ListItems> {
    public static final String QUERY_PARAM = "query";

    private String mListName;
    private String mQuery;

    public GetListResultsRequest(Context ctx, String listName) {
        super(ListItems.class, ctx, null, null);
        mListName = listName;

        String template = ctx.getString(R.string.list_query);
        mQuery = String.format(template, mListName);

        setOutWrapper("results");
    }

    @Override
    protected String generateQuery() {
        return mQuery;
    }

    @Override
    public ListItems loadDataFromNetwork() throws Exception {
        Map<String, ?> params = getUrlParams();
        String uri = getUrl();
        Gson mapper = getMapper();

        Log.i(TAG, "GET: " + expandQuery(uri, params));
        Log.i(TAG, "H: " + getHeaders());

        byte[] bytes = loadBytes(uri, params);

        if (!ArrayUtils.isEmpty(bytes)) {
            String res = new String(bytes);
            Log.i(TAG, "RES: " + res);

            if (!Strs.isNullOrEmpty(getOutWrapper())) {
                Type listType = new TypeToken<List<String[]>>() {
                }.getType();
                Map<String, Object> rooted = mapper.fromJson(res, Map.class);
                String inner = mapper.toJson(rooted.get(getOutWrapper()));

                List<String[]> values = mapper.fromJson(inner, listType);
                ListItems listItems = transform2Genes(values, mapper);

                String columnHeaders = mapper.toJson(rooted.get("columnHeaders"));
                String[] headers = mapper.fromJson(columnHeaders, String[].class);
                listItems.setFields(Arrays.asList(headers));
                return listItems;
            }
        }
        return null;
    }

    private ListItems transform2Genes(List<String[]> list, Gson mapper) {
        ListItems result = new ListItems();

        if (null != list && !list.isEmpty()) {
            for (String[] features : list) {
                List<String> values = Arrays.asList(features);
                result.add(values);
            }
        }
        return result;
    }
}
