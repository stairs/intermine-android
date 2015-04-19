package org.intermine.app.net.request;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.lang3.ArrayUtils;
import org.intermine.app.util.Strs;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

public class JsonGetRequest<T> extends GetRequest<T> {
    public static final String TAG = JsonGetRequest.class.getSimpleName();

    public final static String CONTENT_TYPE = "application";
    public final static String CONTENT_SUBTYPE = "json";

    public final static String DEFAULT_ROOT = "params";

    private String mOutWrapper;

    public JsonGetRequest(Class<T> clazz, Context ctx, String url, Map<String, ?> params) {
        super(clazz, ctx, url, params);
    }

    public Gson getMapper() {
        return new Gson();
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = super.getHeaders();
        headers.setContentType(new MediaType(CONTENT_TYPE, CONTENT_SUBTYPE));

        return headers;
    }

    @Override
    public T loadDataFromNetwork() throws Exception {
        Map<String, ?> params = getUrlParams();
        String uri = getUrl();
        Gson mapper = getMapper();

        Log.i(TAG, "GET: " + expandQuery(uri, params));
        Log.i(TAG, "H: " + getHeaders());

        byte[] bytes = loadBytes(uri, params);
        T result = null;

        if (!ArrayUtils.isEmpty(bytes)) {
            String res = new String(bytes);
            Log.i(TAG, "RES: " + res);

            if (!Strs.isNullOrEmpty(mOutWrapper)) {
                Map<String, Object> rooted = mapper.fromJson(res, Map.class);
                String inner = mapper.toJson(rooted.get(mOutWrapper));

                result = mapper.fromJson(inner, getResultType());
            } else {
                result = mapper.fromJson(res, getResultType());
            }
        }

        Log.i(TAG, "RES: " + result);

        return result;
    }

    public String getOutWrapper() {
        return mOutWrapper;
    }

    public void setOutWrapper(String wrapper) {
        mOutWrapper = wrapper;
    }
}
