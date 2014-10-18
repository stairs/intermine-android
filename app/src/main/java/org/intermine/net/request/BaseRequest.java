package org.intermine.net.request;

import android.content.Context;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.apache.http.client.HttpClient;
import org.intermine.net.DefaultRetryPolicy;
import org.intermine.net.HttpUtils;
import org.intermine.net.ServerErrorHandler;
import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Siarhei Ivonchik <Siarhei_Ivonchik @ epam.com>
 */
public abstract class BaseRequest<T> extends SpringAndroidSpiceRequest<T> {
    public final static String CONTENT_ENCODING = "UTF-8";

    private Context mContext;
    private String mUrl;
    private Map<String, ?> mUrlParams;

    public BaseRequest(Class<T> clazz, Context ctx, String url, Map<String, ?> params) {
        super(clazz);

        setContext(ctx);
        setUrl(url);
        setUrlParams(params);

        setRetryPolicy(new DefaultRetryPolicy());
    }

    @Override
    public RestTemplate getRestTemplate() {
        HttpClient httpClient = HttpUtils.getNewHttpClient();

        RestTemplate rtp = super.getRestTemplate();
        rtp.setErrorHandler(new ServerErrorHandler());
        rtp.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        return rtp;
    }

    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.setAcceptEncoding(new ContentCodingType(CONTENT_ENCODING));
        headers.setContentEncoding(new ContentCodingType(CONTENT_ENCODING));

        return headers;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Map<String, ?> getUrlParams() {
        return mUrlParams;
    }

    public void setUrlParams(Map<String, ?> urlParams) {
        mUrlParams = urlParams;
    }
}
