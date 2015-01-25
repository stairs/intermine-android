package org.intermine.net.request.post;

import android.content.Context;

import org.intermine.R;
import org.intermine.core.Gene;

import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class AppendGenesToListRequest extends CreateGenesList {
    public AppendGenesToListRequest(Context ctx, String mineName, String listName, List<Gene> genes) {
        super(ctx, mineName, listName, genes);
    }

    @Override
    public String getUrl() {
        return getBaseUrl(mMineName) + getContext().getString(R.string.append_to_lists_path);
    }
}
