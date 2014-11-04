package org.intermine.net.request.post;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.intermine.net.request.BaseRequest;
import org.intermine.util.Strs;
import org.intermine.util.Uris;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.springframework.http.HttpMethod.POST;

/**
 * @author Siarhei Ivonchik <Siarhei_Ivonchik @ epam.com>
 */
public class JsonPostRequest<T, V> extends BaseRequest<T> {
    public final static String CONTENT_TYPE = "application";
    public final static String CONTENT_SUBTYPE = "json";

    public final static String DEFAULT_ROOT = "params";

    private V mObject;
    private String mInWrapper;
    private String mOutWrapper;

    public JsonPostRequest(Class<T> clazz, Context ctx, String url, Map<String, ?> params, V obj) {
        super(clazz, ctx, url, params);
        mObject = obj;
    }

    public Gson getMapper() {
        return new Gson();
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = super.getHeaders();
        headers.setContentType(new MediaType(CONTENT_TYPE, "x-www-form-urlencoded"));

        return headers;
    }

    protected String post(String uriString, Map<String, ?> params, String obj) {
        RestTemplate rtp = getRestTemplate();
        HttpHeaders headers = getHeaders();

        HttpEntity<String> req;
        if (Strs.isNullOrEmpty(obj)) {
            req = new HttpEntity<String>(headers);
        } else {
            req = new HttpEntity<String>(obj, headers);
        }

        ResponseEntity<String> res;

        String uri = Uris.expandQuery(uriString, params);

        res = rtp.exchange(uri, POST, req, String.class);

        return res.getBody();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T loadDataFromNetwork() throws Exception {
        Gson mapper = getMapper();

        V obj = getObject();
        String json = null;

        if (null != obj) {
            if (!Strs.isNullOrEmpty(mInWrapper)) {
                JsonElement root = mapper.toJsonTree(getObject());
                JsonObject jo = new JsonObject();
                jo.add(mInWrapper, root);

                json = mapper.toJson(jo);
            } else {
                json = mapper.toJson(getObject());
            }
        }

        String res = post(getUrl(), getUrlParams(), json);

        if (true == Strs.isNullOrEmpty(res)) {
            return null;
        }

        if (false == Strs.isNullOrEmpty(mOutWrapper)) {
            Map<String, Object> rooted = mapper.fromJson(res, Map.class);
            String inner = mapper.toJson(rooted.get(mOutWrapper));

            return mapper.fromJson(inner, getResultType());
        } else {
            return mapper.fromJson(res, getResultType());
        }
    }

    public String getInWrapper() {
        return mInWrapper;
    }

    public void setInWrapper(String wrapper) {
        mInWrapper = wrapper;
    }

    public String getOutWrapper() {
        return mOutWrapper;
    }

    public void setOutWrapper(String wrapper) {
        mOutWrapper = wrapper;
    }

    public V getObject() {
        return mObject;
    }

    public void setObject(V object) {
        mObject = object;
    }
}
