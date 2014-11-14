package org.intermine.net.request.post;

import android.content.Context;

import org.intermine.R;
import org.intermine.core.Gene;
import org.intermine.net.request.PostAuthRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class AddGenesToFavoritesRequest extends PostAuthRequest<Void> {
    private static final String GENES_FAVORITES_PARAM = "org.intermine.android.favorites.gene";
    private String mMineBaseUrl;
    private List<Gene> mGenes;

    public AddGenesToFavoritesRequest(Context ctx, String baseUrl, List<Gene> genes, String token) {
        super(Void.class, ctx, null, null, null, token);
        mMineBaseUrl = baseUrl;
        mGenes = genes;
    }


    @Override
    public MultiValueMap<String, String> getPost() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        for (Gene gene : mGenes) {
            params.add(GENES_FAVORITES_PARAM, gene.getPrimaryDBId());
        }
        return params;
    }


    @Override
    public String getUrl() {
        return mMineBaseUrl + getContext().getString(R.string.user_preferences_path);
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {
        return null;
    }
}
