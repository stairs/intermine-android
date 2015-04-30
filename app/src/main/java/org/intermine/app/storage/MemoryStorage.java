package org.intermine.app.storage;

import android.content.Context;

import org.intermine.app.core.model.Model;
import org.intermine.app.util.Collections;
import org.intermine.app.util.Strs;

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
        Set<String> mines = getSelectedMineNames();
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
        Set<String> selectedMineNames = getSelectedMineNames();

        if (!Collections.isNullOrEmpty(selectedMineNames)) {
            for (String mineName : selectedMineNames) {
                result.put(mineName, getMineUrl(mineName));
            }
        }
        return result;
    }

    @Override
    public void setMineUrl(String mine, String url) {
        super.setMineUrl(mine, url);

        if (null == mMineNameToUrlMap) {
            mMineNameToUrlMap = initializeMineToBaseUrlMap(getContext());
        }
        mMineNameToUrlMap.put(mine, url);
    }
}
