package org.intermine.net;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.intermine.util.Strs;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ServerErrorHandler implements ResponseErrorHandler {
    public final static String ENCODING = "UTF-8";
    public final static String ERROR_SUFFIX = "_error";

    private Gson mMapper;

    private DefaultResponseErrorHandler mHander;

    public ServerErrorHandler() {
        mHander = new DefaultResponseErrorHandler();
        mMapper = new Gson();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        mHander.handleError(response);
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return mHander.hasError(response);
    }
}
