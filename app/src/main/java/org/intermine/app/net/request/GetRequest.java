package org.intermine.app.net.request;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.content.Context;

import org.intermine.app.util.Uris;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
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

        HttpEntity<String> req = new HttpEntity<>(headers);
        ResponseEntity<byte[]> res;

        URI uri = URI.create(expandQuery(uriString, params));
        res = rtp.exchange(uri, GET, req, byte[].class);
        return res.getBody();
    }

    protected String expandQuery(String uriString, Map<String, ?> params) {
        return Uris.expandQuery(uriString, params);
    }
}
