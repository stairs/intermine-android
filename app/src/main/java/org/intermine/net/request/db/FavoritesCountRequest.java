package org.intermine.net.request.db;

import android.content.Context;

import com.octo.android.robospice.request.SpiceRequest;

import org.intermine.core.GenesList;
import org.intermine.dao.GeneDAO;
import org.intermine.dao.SQLiteGeneDAO;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class FavoritesCountRequest extends GeneDAORequest<Integer> {
    public FavoritesCountRequest(Context ctx) {
        super(Integer.class, ctx);
    }

    @Override
    public Integer loadDataFromNetwork() throws Exception {
        return getGeneDAO().getCount();
    }
}
