package org.intermine.app.net.request;

import android.content.Context;

import org.intermine.app.util.Collections;
import org.intermine.app.util.Strs;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public abstract class PostAuthRequest<T> extends PostRequest<T> {
    protected static final String TOKEN_PARAM = "token";

    protected String mMineName;

    public PostAuthRequest(Class<T> clazz, Context ctx, String url, Map<String, ?> params,
                           MultiValueMap<String, String> post, String mineName) {
        super(clazz, ctx, url, params, post);
        mMineName = mineName;
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

    public String getMineName() {
        return mMineName;
    }
}
