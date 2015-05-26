package org.intermine.app.net;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

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
