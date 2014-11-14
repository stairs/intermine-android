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
public class AppendGenesToListRequest extends CreateGenesList {
    public AppendGenesToListRequest(Context ctx, String mineName, String listName, List<String> ids) {
        super(ctx, mineName, listName, ids);
    }

    @Override
    public String getUrl() {
        return getBaseUrl() + getContext().getString(R.string.append_to_lists_path);
    }
}
