package org.intermine.storage;

import android.content.Context;

import org.intermine.core.model.Model;
import org.intermine.util.Collections;

import java.util.Map;

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
}
