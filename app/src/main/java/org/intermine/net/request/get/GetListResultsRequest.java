package org.intermine.net.request.get;

import android.content.Context;

import org.intermine.R;

import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetListResultsRequest extends GetQueryResultsRequest<List> {
    private String mListName;

    public GetListResultsRequest(Context ctx, String listName) {
        super(List.class, ctx, ctx.getString(R.string.flymine_search_url), null);
        mListName = listName;
        setTemplate(ctx.getString(R.string.list_query));
    }

    @Override
    protected String generateQuery(String template) {
        return String.format(String.format(template, mListName));
    }
}
