package org.intermine.app.net.request.get;

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