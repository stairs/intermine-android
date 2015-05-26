package org.intermine.app.net.request;

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

import org.intermine.app.util.Collections;
import org.intermine.app.util.Strs;

import java.util.Map;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public abstract class JsonGetAuthRequest<T> extends JsonGetRequest<T> {
    private static final String TOKEN_PARAM = "token";
    private String mMineName;

    public JsonGetAuthRequest(Class clazz, Context ctx, String url, Map params, String mine) {
        super(clazz, ctx, url, params);
        mMineName = mine;
    }

    @Override
    public Map<String, String> getUrlParams() {
        Map<String, String> params = Collections.newHashMap();
        String token = getStorage().getUserToken(mMineName);

        if (!Strs.isNullOrEmpty(token)) {
            params.put(TOKEN_PARAM, token);
        }
        return params;
    }

    protected String getBaseUrl() {
        return getStorage().getMineNameToUrlMap().get(mMineName);
    }

    public String getMineName() {
        return mMineName;
    }
}
