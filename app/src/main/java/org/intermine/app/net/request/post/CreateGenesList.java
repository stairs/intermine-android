package org.intermine.app.net.request.post;

import android.content.Context;

import org.intermine.app.R;
import org.intermine.app.core.Gene;
import org.intermine.app.net.request.PostAuthRequest;
import org.intermine.app.util.Collections;
import org.intermine.app.util.Strs;
import org.intermine.app.util.Uris;
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
    private List<Gene> mGenes;

    public CreateGenesList(Context ctx, String mineName, String listName, List<Gene> genes) {
        super(Void.class, ctx, null, null, null, mineName);
        mListName = listName;
        mGenes = genes;
    }

    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = super.getHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return headers;
    }

    @Override
    public String getUrl() {
        return getBaseUrl(mMineName) + getContext().getString(R.string.lists_path);
    }

    @Override
    public Map<String, String> getUrlParams() {
        Map<String, String> params = super.getUrlParams();
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
        String post = generateBody();

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

    protected String generateBody() {
        List<String> genesIds = Collections.newArrayList();

        if (!Collections.isNullOrEmpty(mGenes)) {
            for (Gene gene : mGenes) {
                genesIds.add(gene.getPrimaryDBId());
            }
        }
        return Strs.join(genesIds, ", ");
    }
}