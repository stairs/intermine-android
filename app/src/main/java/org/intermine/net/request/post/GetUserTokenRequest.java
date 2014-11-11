package org.intermine.net.request.post;

import android.content.Context;

import com.google.gson.Gson;

import org.intermine.R;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetUserTokenRequest extends PostBaseAuthorizationRequest<String> {
    public static final String TOKEN_PARAM = "token";
    public static final String TOKEN_TYPE_PARAM = "type";
    public static final String TOKEN_MESSAGE_PARAM = "message";

    public static final String DEFAULT_TOKEN_TYPE_VALUE = "perm";
    public static final String TOKEN_MESSAGE_VALUE = "InterMine Android App";

    private String mMineBaseUrl;

    public GetUserTokenRequest(Context ctx, String mineBaseUrl, String username, String password) {
        super(String.class, ctx, null, null, null);
        setUsername(username);
        setPassword(password);

        mMineBaseUrl = mineBaseUrl;
    }

    @Override
    public String getUrl() {
        return mMineBaseUrl + getContext().getString(R.string.get_token_path);
    }

    @Override
    public MultiValueMap<String, String> getPost() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add(TOKEN_TYPE_PARAM, DEFAULT_TOKEN_TYPE_VALUE);
        params.add(TOKEN_MESSAGE_PARAM, TOKEN_MESSAGE_VALUE);
        return super.getPost();
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
