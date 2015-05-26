package org.intermine.app.sqlite;

/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static org.intermine.app.sqlite.InterMineDatabaseContract.Favorites;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class InterMineDatabaseHelper extends SQLiteOpenHelper {
    private static final String INTERMINE_DATABASE_NAME = "intermine.db";
    private static final int INTERMINE_DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Favorites.TABLE_NAME + " (" +
                    Favorites._ID + " INTEGER PRIMARY KEY," +
                    Favorites.COLUMN_NAME_GENE_ID + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_NAME_SYMBOL + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_NAME_LOCATED_ON + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_NAME_LOCATION_START + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_NAME_LOCATION_END + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_NAME_LOCATION_STRAND + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_SECONDARY_ID + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_ORGANISM_NAME + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_ORGANISM_SHORT_NAME + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_ONTOLOGY_TERM + TEXT_TYPE + COMMA_SEP +
                    Favorites.COLUMN_MINE + TEXT_TYPE + COMMA_SEP +
                    "UNIQUE(" + Favorites.COLUMN_NAME_GENE_ID + ") ON CONFLICT REPLACE" +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Favorites.TABLE_NAME;

    public InterMineDatabaseHelper(Context context) {
        super(context, INTERMINE_DATABASE_NAME, null, INTERMINE_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
