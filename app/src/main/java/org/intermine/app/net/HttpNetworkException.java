package org.intermine.app.net;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

public class HttpNetworkException extends RestClientException {
    private HttpStatus mStatusCode;
    private HttpHeaders mHeaders;
    private String mErrorMessage;

    public HttpNetworkException(String msg) {
        super(msg);
    }

    public HttpNetworkException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public HttpHeaders getHeaders() {
        return mHeaders;
    }

    public void setHeaders(HttpHeaders headers) {
        mHeaders = headers;
    }

    public HttpStatus getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        mStatusCode = statusCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }
}
