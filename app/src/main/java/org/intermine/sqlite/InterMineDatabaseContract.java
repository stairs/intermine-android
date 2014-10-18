package org.intermine.sqlite;

import android.provider.BaseColumns;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class InterMineDatabaseContract {
    private InterMineDatabaseContract() {
    }

    public static abstract class Favorites implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_NAME_GENE_ID = "gene_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SYMBOL = "symbol";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_LOCATION_START = "location_start";
        public static final String COLUMN_NAME_LOCATION_END = "location_end";
        public static final String COLUMN_NAME_LOCATION_STRAND = "location_strand";
        public static final String COLUMN_SECONDARY_ID = "secondary_id";
        public static final String COLUMN_ORGANISM_NAME = "organism_name";
        public static final String COLUMN_ORGANISM_SHORT_NAME = "organism_short_name";
        public static final String COLUMN_ONTOLOGY_TERM = "ontology_term";
        public static final String COLUMN_NAME_LOCATED_ON = "located_on";
        public static final String COLUMN_MINE = "mine";
    }
}
