package org.intermine.net.request.get;

import android.content.Context;

import org.intermine.net.request.BaseRequest;
import org.intermine.util.Uris;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;

public abstract class GetRequest<T> extends BaseRequest<T> {
    public static final String FORMAT_PARAM = "format";

    public GetRequest(Class<T> clazz, Context ctx, String url, Map<String, ?> params) {
        super(clazz, ctx, url, params);
    }

    protected byte[] loadBytes(String uriString, Map<String, ?> params) {
        RestTemplate rtp = getRestTemplate();
        HttpHeaders headers = getHeaders();

        HttpEntity<String> req = new HttpEntity<String>(headers);
        ResponseEntity<byte[]> res;

        String uri = expandQuery(uriString, params);

        try {
            res = rtp.exchange(URLDecoder.decode(uri, "UTF-8"), GET, req, byte[].class, params);
            return res.getBody();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //res = rtp.exchange(uri, GET, req, byte[].class, params);
        return null;
    }

    protected String expandQuery(String uriString, Map<String, ?> params) {
        return Uris.expandQuery(uriString, params);
    }
}
