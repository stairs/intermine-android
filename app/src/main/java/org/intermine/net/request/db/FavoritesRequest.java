package org.intermine.net.request.db;

import android.content.Context;

import com.octo.android.robospice.request.SpiceRequest;

import org.intermine.core.Gene;
import org.intermine.core.GenesList;
import org.intermine.dao.GeneDAO;
import org.intermine.dao.SQLiteGeneDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class FavoritesRequest extends GeneDAORequest<GenesList> {
    private int mStart;
    private int mCount;

    public FavoritesRequest(Context ctx, int start, int count) {
        super(GenesList.class, ctx);
        mStart = start;
        mCount = count;
    }

    @Override
    public GenesList loadDataFromNetwork() throws Exception {
        return getGeneDAO().fetchAll(mStart, mCount);
    }
}
