package org.intermine.storage;

import android.content.Context;

import org.intermine.core.model.Model;
import org.intermine.util.Collections;
import org.intermine.util.Strs;

import java.util.Map;
import java.util.Set;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class MemoryStorage extends BaseStorage {
    private Map<String, Model> mMineToModelMap;

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
}
