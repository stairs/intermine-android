package org.intermine.net.request.get;

import android.content.Context;

import org.intermine.R;
import org.intermine.core.GenesDataList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetListResultsRequest extends GetQueryResultsRequest<List> {
    private String mListName;

    public GetListResultsRequest(Context ctx, String listName) {
        super(List.class, ctx, null, null);
        mListName = listName;
        setTemplate(ctx.getString(R.string.list_query));
    }

    @Override
    protected String generateQuery(String template) {
        return String.format(String.format(template, mListName));
    }
}
