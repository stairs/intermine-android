package org.intermine.storage;

import android.content.Context;

import org.intermine.R;
import org.intermine.core.model.Model;
import org.intermine.util.Collections;
import org.intermine.util.Strs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class MemoryStorage extends BaseStorage {
    private Map<String, Model> mMineToModelMap;
    private volatile Map<String, String> mMineNameToUrlMap;

    public MemoryStorage(Context ctx) {
        super(ctx);
    }

    @Override
    public Model getMineModel(String mineName) {
        if (null != mMineToModelMap) {
            return mMineToModelMap.get(mineName);
        }
        return null;
    }

    @Override
    public Map<String, Model> getMineToModelMap() {
        return mMineToModelMap;
    }

    @Override
    public void addMineModel(String mineName, Model model) {
        if (null == mMineToModelMap) {
            mMineToModelMap = Collections.newHashMap();
        }
        mMineToModelMap.put(mineName, model);
    }

    @Override
    public Map<String, String> getMineToUserTokenMap() {
        Set<String> mines = getMineNames();
        Map<String, String> result = Collections.newHashMap();

        for (String mine : mines) {
            String token = getUserToken(mine);

            if (!Strs.isNullOrEmpty(token)) {
                result.put(mine, token);
            }
        }
        return result;
    }

    @Override
    public Map<String, String> getMineNameToUrlMap() {
        if (null == mMineNameToUrlMap) {
            mMineNameToUrlMap = initializeMineToBaseUrlMap(getContext());
        }
        return mMineNameToUrlMap;
    }

    private synchronized Map<String, String> initializeMineToBaseUrlMap(Context ctx) {
        Map<String, String> result = Collections.newHashMap();

        List<String> defaultMineNames = Arrays.asList(ctx.getResources().getStringArray(R.array.mines_names));
        String[] defaultMineBaseUrls = ctx.getResources().getStringArray(R.array.mines_urls);

        if (null != defaultMineNames && null != defaultMineBaseUrls &&
                defaultMineNames.size() == defaultMineBaseUrls.length) {
            int length = defaultMineNames.size();

            for (int i = 0; i < length; i++) {
                result.put(defaultMineNames.get(i), defaultMineBaseUrls[i]);
            }
        }

        Set<String> allMines = new HashSet<>(getMineNames());
        allMines.removeAll(defaultMineNames);

        if (!Collections.isNullOrEmpty(allMines)) {
            for (String mineName : allMines) {
                result.put(mineName, getMineUrl(mineName));
            }
        }
        return result;
    }

    @Override
    public void setMineUrl(String mine, String url) {
        super.setMineUrl(mine, url);

        if (null != mMineNameToUrlMap) {
            mMineNameToUrlMap.put(mine, url);
        }
    }
}
