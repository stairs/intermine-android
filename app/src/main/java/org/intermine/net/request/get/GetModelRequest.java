package org.intermine.net.request.get;

import android.content.Context;

import com.google.common.collect.Maps;

import org.intermine.R;
import org.intermine.core.model.Model;

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
        Map<String, String> params = Maps.newHashMap();
        params.put(FORMAT_PARAM, CONTENT_SUBTYPE);
        return params;
    }
}
