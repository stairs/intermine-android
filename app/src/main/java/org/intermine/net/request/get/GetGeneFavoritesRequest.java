package org.intermine.net.request.get;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.octo.android.robospice.request.SpiceRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.intermine.R;
import org.intermine.core.Gene;
import org.intermine.core.GenesList;
import org.intermine.dao.GeneDAO;
import org.intermine.dao.SQLiteGeneDAO;
import org.intermine.net.request.JsonGetAuthRequest;
import org.intermine.net.request.db.GeneDAORequest;
import org.intermine.util.Mines;
import org.intermine.util.Strs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetGeneFavoritesRequest extends JsonGetAuthRequest<List> {
    private final String GENE_PREFERENCES_KEY;

    public GetGeneFavoritesRequest(Context ctx, String mineName) {
        super(List.class, ctx, null, null, mineName);
        setOutWrapper("preferences");
        GENE_PREFERENCES_KEY = ctx.getString(R.string.gene_favorites_key);
    }

    @Override
    public String getUrl() {
        return getBaseUrl() + getContext().getString(R.string.user_preferences_path);
    }

    @Override
    public List<String> loadDataFromNetwork() throws Exception {
        Map<String, ?> params = getUrlParams();
        String uri = getUrl();
        Gson mapper = getMapper();

        byte[] bytes = loadBytes(uri, params);

        if (!ArrayUtils.isEmpty(bytes)) {
            String res = new String(bytes);

            if (!Strs.isNullOrEmpty(getOutWrapper())) {
                Map<String, Object> rooted = mapper.fromJson(res, Map.class);
                String inner = mapper.toJson(rooted.get(getOutWrapper()));

                Map<String, Object> preferencesMap = mapper.fromJson(inner, Map.class);
                String geneFavorites = (String) preferencesMap.get(GENE_PREFERENCES_KEY);
            }
        }
        return Collections.emptyList();
    }
}
