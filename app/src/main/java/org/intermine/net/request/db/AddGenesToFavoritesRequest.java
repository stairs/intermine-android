package org.intermine.net.request.db;

import android.content.Context;

import org.intermine.core.Gene;
import org.intermine.core.GenesList;

import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class AddGenesToFavoritesRequest extends GeneDAORequest<Void> {
    private List<Gene> mGenes;

    public AddGenesToFavoritesRequest(Context ctx, List<Gene> genes) {
        super(Void.class, ctx);
        mGenes = genes;
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {
        getGeneDAO().saveAll(mGenes);
        return null;
    }
}
