package org.intermine.net.request.post;

import android.content.Context;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public abstract class PostBaseAuthorizationRequest<T> extends PostRequest<T> {
    private String mUsername;
    private String mPassword;

    public PostBaseAuthorizationRequest(Class clazz, Context ctx, String url, Map params, MultiValueMap post) {
        super(clazz, ctx, url, params, post);
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = super.getHeaders();
        headers.setAuthorization(new HttpBasicAuthentication(mUsername, mPassword));
        return headers;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
