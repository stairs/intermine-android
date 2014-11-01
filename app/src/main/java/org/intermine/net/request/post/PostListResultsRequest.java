package org.intermine.net.request.post;

import android.content.Context;
import android.util.Log;

import org.intermine.R;
import org.intermine.core.ListItems;
import org.intermine.util.Collections;

import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class PostListResultsRequest extends JsonPostRequest<ListItems, Object> {
    public static final String QUERY_PARAM = "query";

    private String mListName;
    private String mQuery;

    public PostListResultsRequest(Context ctx, String listName) {
        super(ListItems.class, ctx, null, null, null);
        mListName = listName;

        String template = ctx.getString(R.string.list_query);
        mQuery = String.format(template, mListName);

        setInWrapper(DEFAULT_ROOT);
        setOutWrapper("results");
    }

    @Override
    public String getUrl() {
        Context ctx = getContext();
        return ctx.getString(R.string.flymine_url) + ctx.getString(R.string.search_path);
    }

    @Override
    public Object getObject() {
        Map<String, String> params = Collections.newHashMap();
        params.put("format", "json");
        params.put(QUERY_PARAM, mQuery);
        params.put(QUERY_PARAM, mQuery);

        return params;
    }

    //    @Override
//    public ListItems loadDataFromNetwork() throws Exception {
//        Map<String, ?> params = getUrlParams();
//        String uri = getUrl();
//        Gson mapper = getMapper();
//
//        Log.i(TAG, "GET: " + expandQuery(uri, params));
//        Log.i(TAG, "H: " + getHeaders());
//
//        byte[] bytes = loadBytes(uri, params);
//
//        if (!ArrayUtils.isEmpty(bytes)) {
//            String res = new String(bytes);
//            Log.i(TAG, "RES: " + res);
//
//            if (!Strs.isNullOrEmpty(getOutWrapper())) {
//                Type listType = new TypeToken<List<String[]>>() {
//                }.getType();
//                Map<String, Object> rooted = mapper.fromJson(res, Map.class);
//                String inner = mapper.toJson(rooted.get(getOutWrapper()));
//
//                List<String[]> values = mapper.fromJson(inner, listType);
//                return transform2Genes(values, mapper);
//            }
//        }
//        return null;
//    }
//
//    private ListItems transform2Genes(List<String[]> list, Gson mapper) {
//        ListItems result = new ListItems();
//
//        List<String> fields = new ArrayList<String>();
//        fields.add("1");
//        fields.add("2");
//        fields.add("3");
//        fields.add("4");
//        result.setFields(fields);
//
//        if (null != list && !list.isEmpty()) {
//            for (String[] features : list) {
//                List<String> values = Arrays.asList(features);
//                result.add(values);
//            }
//        }
//        return result;
//    }
}
