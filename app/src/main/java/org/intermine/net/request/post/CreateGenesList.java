package org.intermine.net.request.post;

import android.content.Context;

import org.intermine.R;
import org.intermine.net.request.PostAuthRequest;
import org.intermine.util.Collections;
import org.intermine.util.Strs;
import org.intermine.util.Uris;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.POST;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class CreateGenesList extends PostAuthRequest<Void> {
    private static final String LIST_NAME_PARAM = "name";
    private static final String LIST_TYPE_PARAM = "type";
    private static final String LIST_TYPE_VALUE = "Gene";

    private String mListName;
    private List<String> mGeneIds;

    public CreateGenesList(Context ctx, String mineName, String listName, List<String> ids) {
        super(Void.class, ctx, null, null, null, mineName);
        mListName = listName;
        mGeneIds = ids;
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = super.getHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return headers;
    }

    @Override
    public String getUrl() {
        return getBaseUrl() + getContext().getString(R.string.lists_path);
    }

    @Override
    public Map<String, ?> getUrlParams() {
        Map<String, String> params = Collections.newHashMap();
        params.put(LIST_NAME_PARAM, mListName);
        params.put(LIST_TYPE_PARAM, LIST_TYPE_VALUE);
        return params;
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {
        RestTemplate rtp = getRestTemplate();
        HttpHeaders headers = getHeaders();
        String uriString = getUrl();
        Map<String, ?> params = getUrlParams();
        String post = Strs.join(mGeneIds, ", ");

        HttpEntity<?> req;
        if (null != post) {
            req = new HttpEntity<Object>(post, headers);
        } else {
            req = new HttpEntity<String>(headers);
        }

        ResponseEntity<String> res;

        String uri = Uris.expandQuery(uriString, params);

        res = rtp.exchange(uri, POST, req, String.class);
        return null;
    }
}
