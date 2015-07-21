package org.intermine.app.net.request.get;

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

import org.intermine.app.R;
import org.intermine.app.net.request.JsonGetRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetTypeFieldsRequest extends JsonGetRequest<GetTypeFieldsRequest.TypeFields> {
    private String mMineName;

    public GetTypeFieldsRequest(Context ctx, String mineName) {
        super(TypeFields.class, ctx, null, null);
        mMineName = mineName;
        setOutWrapper("classes");
    }

    @Override
    public String getUrl() {
        return getBaseUrl(mMineName) + getContext().getResources().getString(R.string.summary_fields);
    }

    @Override
    public Map<String, ?> getUrlParams() {
        Map<String, String> params = new HashMap<>();
        return params;
    }

    @Override
    public TypeFields loadDataFromNetwork() throws Exception {
        TypeFields result = super.loadDataFromNetwork();
        getStorage().setTypeFields(mMineName, result);
        return result;
    }

    public static class TypeFields extends HashMap<String, List<String>> {
    }
}