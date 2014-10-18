package org.intermine.dao;

import org.intermine.core.Gene;
import org.intermine.core.GenesList;

import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public interface GeneDAO {
    void save(Gene gene);

    void saveAll(List<Gene> genes);

    GenesList fetchAll(int from, int count);

    void delete(Gene gene);

    void deleteAll(List<Gene> genes);

    int getCount();
}
