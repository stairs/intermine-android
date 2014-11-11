package org.intermine.util;

import android.content.Context;

import org.intermine.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class Mines {
    private static Map<String, String> mMinesToBaseUrl;

    public static Map<String, String> getMineToBaseUrlMap(Context ctx) {
        if (null == mMinesToBaseUrl) {
            initializeMineToBaseUrlMap(ctx);
        }
        return mMinesToBaseUrl;
    }

    public static String getMineBaseUrl(Context ctx, String mineName) {
        if (null == mMinesToBaseUrl) {
            initializeMineToBaseUrlMap(ctx);
        }
        return mMinesToBaseUrl.get(mineName);
    }

    private static void initializeMineToBaseUrlMap(Context ctx) {
        mMinesToBaseUrl = Collections.newHashMap();
        String[] mineNames = ctx.getResources().getStringArray(R.array.mines_names);
        String[] mineBaseUrls = ctx.getResources().getStringArray(R.array.mines_urls);

        if (null != mineNames && null != mineBaseUrls && mineNames.length == mineBaseUrls.length) {
            int length = mineNames.length;

            for (int i = 0; i < length; i++) {
                mMinesToBaseUrl.put(mineNames[i], mineBaseUrls[i]);
            }
        }
    }
}
