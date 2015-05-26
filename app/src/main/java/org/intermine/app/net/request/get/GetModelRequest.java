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
import org.intermine.app.core.model.Model;
import org.intermine.app.net.request.JsonGetRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class GetModelRequest extends JsonGetRequest<Model> {
    private String mMineBaseUrl;

    public GetModelRequest(Context ctx, String mineBaseUrl) {
        super(Model.class, ctx, null, null);
        mMineBaseUrl = mineBaseUrl;
    }

    @Override
    public String getUrl() {
        return mMineBaseUrl + getContext().getResources().getString(R.string.model_path);
    }

    @Override
    public Map<String, ?> getUrlParams() {
        Map<String, String> params = new HashMap<>();
        params.put(FORMAT_PARAM, CONTENT_SUBTYPE);
        return params;
    }
}