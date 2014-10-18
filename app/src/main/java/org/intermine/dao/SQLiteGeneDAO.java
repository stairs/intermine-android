package org.intermine.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import org.intermine.core.Gene;
import org.intermine.core.GenesList;
import org.intermine.sqlite.InterMineDatabaseHelper;

import java.util.List;

import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_MINE;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_NAME_DESCRIPTION;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_NAME_GENE_ID;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_NAME_LOCATED_ON;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_NAME_LOCATION_END;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_NAME_LOCATION_START;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_NAME_LOCATION_STRAND;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_NAME_NAME;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_NAME_SYMBOL;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_ONTOLOGY_TERM;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_ORGANISM_NAME;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_SECONDARY_ID;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.COLUMN_ORGANISM_SHORT_NAME;
import static org.intermine.sqlite.InterMineDatabaseContract.Favorites.TABLE_NAME;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class SQLiteGeneDAO implements GeneDAO {
    private static final String TAG = SQLiteGeneDAO.class.getSimpleName();
    public static final String[] ALL_COLUMNS = {BaseColumns._ID, COLUMN_NAME_GENE_ID,
            COLUMN_NAME_NAME, COLUMN_NAME_SYMBOL, COLUMN_NAME_DESCRIPTION,
            COLUMN_NAME_LOCATION_START, COLUMN_NAME_LOCATION_END, COLUMN_NAME_LOCATED_ON,
            COLUMN_NAME_LOCATION_STRAND, COLUMN_SECONDARY_ID, COLUMN_ORGANISM_NAME,
            COLUMN_ORGANISM_SHORT_NAME, COLUMN_ONTOLOGY_TERM, COLUMN_MINE};
    private static final String FAVORITES_COUNT_QUERY = "SELECT COUNT (*) FROM " + TABLE_NAME;

    private SQLiteOpenHelper mDatabaseHelper;

    public SQLiteGeneDAO(Context ctx) {
        mDatabaseHelper = new InterMineDatabaseHelper(ctx);
    }

    @Override
    public void save(Gene gene) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        try {
            insertGene(db, gene);
        } finally {
            db.close();
        }
    }

    @Override
    public void saveAll(List<Gene> genes) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        //TODO: batch update required
        try {
            for (Gene gene : genes) {
                insertGene(db, gene);
            }
        } finally {
            db.close();
        }
    }

    @Override
    public GenesList fetchAll(int from, int count) {
        GenesList genes = new GenesList();
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, null, null);

        if (null != cursor && cursor.moveToPosition(from)) {
            try {
                while (!cursor.isAfterLast() && cursor.getPosition() < from + count) {
                    genes.add(cursor2Gene(cursor));
                    cursor.moveToNext();
                }
            } finally {
                if (null != cursor && !cursor.isClosed()) {
                    cursor.close();
                }
                db.close();
            }
        }
        return genes;
    }

    @Override
    public void delete(Gene gene) {

    }

    @Override
    public void deleteAll(List<Gene> genes) {

    }

    @Override
    public int getCount() {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(FAVORITES_COUNT_QUERY, null);
        int count = 0;

        if (null != cursor && cursor.moveToFirst()) {
            try {
                count = cursor.getInt(0);
            } finally {
                if (null != cursor && !cursor.isClosed()) {
                    cursor.close();
                }
                db.close();
            }
        }
        return count;
    }

    protected Gene cursor2Gene(Cursor cursor) {
        Gene gene = new Gene();
        gene.setId(cursor.getLong(0));
        gene.setPrimaryDBId(cursor.getString(1));
        gene.setName(cursor.getString(2));
        gene.setSymbol(cursor.getString(3));
        gene.setDescription(cursor.getString(4));
        gene.setLocationStart(cursor.getString(5));
        gene.setLocationEnd(cursor.getString(6));
        gene.setLocatedOn(cursor.getString(7));
        gene.setLocationStrand(cursor.getString(8));
        gene.setSecondaryIdentifier(cursor.getString(9));
        gene.setOrganismName(cursor.getString(10));
        gene.setOrganismShortName(cursor.getString(11));
        gene.setOntologyTerm(cursor.getString(12));
        gene.setMine(cursor.getString(13));
        return gene;
    }

    protected void insertGene(SQLiteDatabase db, Gene gene) {
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_GENE_ID, gene.getPrimaryDBId());
            values.put(COLUMN_NAME_NAME, gene.getName());
            values.put(COLUMN_NAME_SYMBOL, gene.getSymbol());
            values.put(COLUMN_NAME_DESCRIPTION, gene.getDescription());
            values.put(COLUMN_NAME_LOCATED_ON, gene.getLocatedOn());
            values.put(COLUMN_NAME_LOCATION_START, gene.getLocationStart());
            values.put(COLUMN_NAME_LOCATION_END, gene.getLocationEnd());
            values.put(COLUMN_NAME_LOCATION_STRAND, gene.getLocationStrand());
            values.put(COLUMN_SECONDARY_ID, gene.getSecondaryIdentifier());
            values.put(COLUMN_ORGANISM_NAME, gene.getOrganismName());
            values.put(COLUMN_ORGANISM_SHORT_NAME, gene.getOrganismShortName());
            values.put(COLUMN_ONTOLOGY_TERM, gene.getOntologyTerm());
            values.put(COLUMN_MINE, gene.getMine());

            long id = db.insert(TABLE_NAME, null, values);
            gene.setId(id);
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to save gene %s to favorites!", gene));
        }
    }
}
