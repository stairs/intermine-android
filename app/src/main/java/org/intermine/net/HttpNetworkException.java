package org.intermine.net;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

/**
 * @author Siarhei Ivonchik <Siarhei_Ivonchik @ epam.com>
 */
public class HttpNetworkException extends RestClientException {
    private static final long serialVersionUID = 3249097104680843270L;

    private HttpStatus mStatus;
    private HttpHeaders mHeaders;
    private String mHttpMessage;
    private Error mError;

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

    public HttpStatus getStatus() {
        return mStatus;
    }

    public void setStatus(HttpStatus status) {
        mStatus = status;
    }

    public String getHttpMessage() {
        return mHttpMessage;
    }

    public void setHttpMessage(String httpMessage) {
        mHttpMessage = httpMessage;
    }

    public Error getError() {
        return mError;
    }

    public void setError(Error error) {
        mError = error;
    }
}
