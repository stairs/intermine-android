package org.intermine.app.net;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.io.IOUtils;
import org.intermine.app.util.Strs;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ServerErrorHandler implements ResponseErrorHandler {
    public final static String DEFAULT_ENCODING = "UTF-8";
    public final static String ERROR_KEY = "error";

    private DefaultResponseErrorHandler mHandler;
    private Gson mMapper;

    public ServerErrorHandler() {
        mHandler = new DefaultResponseErrorHandler();
        mMapper = new Gson();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        InputStream inputStream = response.getBody();
        String body = Strs.nullToEmpty(IOUtils.toString(inputStream, DEFAULT_ENCODING));
        String errorMessage = null;

        try {
            Map<String, String> map =
                    mMapper.fromJson(body, Map.class);

            if (null != map) {
                errorMessage = map.get(ERROR_KEY);
            }
        } catch (JsonSyntaxException ex) {
        }

        HttpNetworkException e = new HttpNetworkException(response.getStatusText());
        e.setErrorMessage(Strs.isNullOrEmpty(errorMessage) ? body : errorMessage);
        e.setHeaders(response.getHeaders());
        e.setStatusCode(response.getStatusCode());
        throw e;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return mHandler.hasError(response);
    }
}