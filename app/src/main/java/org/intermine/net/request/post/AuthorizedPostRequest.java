package org.intermine.net.request.post;

import android.content.Context;

import org.intermine.util.Collections;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public abstract class AuthorizedPostRequest<T> extends PostRequest<T> {
    protected static final String TOKEN_PARAM = "token";

    private String mToken;

    public AuthorizedPostRequest(Class<T> clazz, Context ctx, String url, Map<String, ?> params,
                                 MultiValueMap<String, String> post, String token) {
        super(clazz, ctx, url, params, post);
        mToken = token;
    }

    @Override
    public Map<String, ?> getUrlParams() {
        Map<String, String> params = Collections.newHashMap();
        params.put(TOKEN_PARAM, mToken);
        return super.getUrlParams();
    }
}
