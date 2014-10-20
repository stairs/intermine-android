package org.intermine.net.request.get;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.ArrayUtils;
import org.intermine.R;
import org.intermine.core.Gene;
import org.intermine.core.GenesList;
import org.intermine.util.Strs;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetListResultsRequest extends GetQueryResultsRequest<GenesList> {
    private String mListName;

    public GetListResultsRequest(Context ctx, String listName) {
        super(List.class, ctx, ctx.getString(R.string.flymine_search_url), null);
        mListName = listName;
        setTemplate(ctx.getString(R.string.list_query));
    }

    @Override
    protected String generateQuery(String template) {
        return String.format(String.format(template, mListName));
    }

    @Override
    public GenesList loadDataFromNetwork() throws Exception {
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
                return transform2Genes(values, mapper);
            }
        }
        return null;
    }

    private GenesList transform2Genes(List<String[]> list, Gson mapper) {
        GenesList result = new GenesList();

        if (null != list && !list.isEmpty()) {
            for (String[] features : list) {
                result.add(transform2Gene(features));
            }
        }
        return result;
    }

    private Gene transform2Gene(String[] features) {
        Gene gene = new Gene();
        gene.setSecondaryIdentifier(features[0]);
        gene.setSymbol(features[1]);
        gene.setPrimaryDBId(features[2]);
        gene.setOrganismName(features[3]);
        return gene;
    }
}
