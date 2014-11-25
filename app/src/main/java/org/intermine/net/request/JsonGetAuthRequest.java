package org.intermine.net.request;

import android.content.Context;

import org.intermine.util.Collections;
import org.intermine.util.Mines;
import org.intermine.util.Strs;

import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public abstract class JsonGetAuthRequest<T> extends JsonGetRequest<T> {
    private static final String TOKEN_PARAM = "token";
    private String mMineName;

    public JsonGetAuthRequest(Class clazz, Context ctx, String url, Map params, String mine) {
        super(clazz, ctx, url, params);
        mMineName = mine;
    }

    @Override
    public Map<String, String> getUrlParams() {
        Map<String, String> params = Collections.newHashMap();
        String token = getStorage().getUserToken(mMineName);

        if (!Strs.isNullOrEmpty(token)) {
            params.put(TOKEN_PARAM, token);
        }
        return params;
    }

    protected String getBaseUrl() {
        return Mines.getMineBaseUrl(getContext(), mMineName);
    }

    public String getMineName() {
        return mMineName;
    }
}
