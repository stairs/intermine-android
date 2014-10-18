package org.intermine.net.request.db;

import android.content.Context;

import com.octo.android.robospice.request.SpiceRequest;

import org.intermine.core.GenesList;
import org.intermine.dao.GeneDAO;
import org.intermine.dao.SQLiteGeneDAO;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public abstract class GeneDAORequest<T> extends SpiceRequest<T> {
    private GeneDAO mGeneDAO;
    private Context mContext;

    public GeneDAORequest(Class<T> clazz, Context ctx) {
        super(clazz);
        mContext = ctx;
        mGeneDAO = new SQLiteGeneDAO(mContext);
    }

    protected GeneDAO getGeneDAO() {
        return mGeneDAO;
    }
}
