package org.intermine.app.net.request.post;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.content.Context;

import com.google.gson.Gson;

import org.intermine.app.R;
import org.intermine.app.net.request.PostRequest;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetUserTokenRequest extends PostRequest<String> {
    public static final String TOKEN_PARAM = "token";
    public static final String TOKEN_TYPE_PARAM = "type";
    public static final String TOKEN_MESSAGE_PARAM = "message";

    public static final String DEFAULT_TOKEN_TYPE_VALUE = "api";
    public static final String TOKEN_MESSAGE_VALUE = "InterMine Android App";

    private String mUsername;
    private String mPassword;

    private String mMineName;

    public GetUserTokenRequest(Context ctx, String mineName, String username, String password) {
        super(String.class, ctx, null, null, null);
        mUsername = username;
        mPassword = password;

        mMineName = mineName;
    }

    @Override
    public String getUrl() {
        return getBaseUrl(mMineName) + getContext().getString(R.string.get_token_path);
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = super.getHeaders();
        headers.setAuthorization(new HttpBasicAuthentication(mUsername, mPassword));
        return headers;
    }

    @Override
    public MultiValueMap<String, String> getPost() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(TOKEN_TYPE_PARAM, DEFAULT_TOKEN_TYPE_VALUE);
        params.add(TOKEN_MESSAGE_PARAM, TOKEN_MESSAGE_VALUE);
        return params;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        String json = post();

        Gson mapper = getMapper();
        Map<String, String> map = mapper.fromJson(json, Map.class);
        String token = map.get(TOKEN_PARAM);
        return token;
    }

    protected Gson getMapper() {
        return new Gson();
    }
}
