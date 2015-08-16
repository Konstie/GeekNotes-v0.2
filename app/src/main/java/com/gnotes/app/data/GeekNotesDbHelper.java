package com.gnotes.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GeekNotesDbHelper {
    private static final int DB_VERSION = 1;

    static final String DB_NAME = "geeknotes.db";

    private SQLiteDatabase database;

    private DBHelper dbHelper;

    public GeekNotesDbHelper(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public DBHelper getCoreDatabase() {
        return dbHelper;
    }

    public void insertData(String title, String category, String extraField, String extraInfo) {
        ContentValues cv = new ContentValues();
        cv.put(GeekNotesContract.GeekEntry.COLUMN_TITLE, title);
        cv.put(GeekNotesContract.GeekEntry.COLUMN_CATEGORY, category);
        cv.put(GeekNotesContract.GeekEntry.COLUMN_INFOTYPE, extraField);
        cv.put(GeekNotesContract.GeekEntry.COLUMN_INFO, extraInfo);
        database.insert(GeekNotesContract.GeekEntry.TABLE_NAME, null, cv);
    }

    public void updateData(String sourceTitle, String newTitle, String newExtra, String newCategory) {
        ContentValues cv = new ContentValues();
        cv.put(GeekNotesContract.GeekEntry.COLUMN_TITLE, newTitle);
        cv.put(GeekNotesContract.GeekEntry.COLUMN_INFO, newExtra);
        cv.put(GeekNotesContract.GeekEntry.COLUMN_CATEGORY, newCategory);
        cv.put(GeekNotesContract.GeekEntry.COLUMN_ARTICLE_INFO, "");
        cv.put(GeekNotesContract.GeekEntry.COLUMN_ARTICLE_POSTERLINK, "");
        cv.put(GeekNotesContract.GeekEntry.COLUMN_ARTICLE_RANK, "");
        cv.put(GeekNotesContract.GeekEntry.COLUMN_ARTICLE_IMDB_INFO, "");
        cv.put(GeekNotesContract.GeekEntry.COLUMN_TRANSLATION, "");
        database.update(GeekNotesContract.GeekEntry.TABLE_NAME, cv, GeekNotesContract.GeekEntry.COLUMN_TITLE + " = ?",
                new String[]{sourceTitle});
    }

    public void resetWikipediaArticlePlot(String itemTitle) {
        ContentValues cv = new ContentValues();
        cv.put(GeekNotesContract.GeekEntry.COLUMN_ARTICLE_INFO, "42. Просто 42."); // заглушка
        database.update(GeekNotesContract.GeekEntry.TABLE_NAME, cv, GeekNotesContract.GeekEntry.COLUMN_TITLE + " = ?",
                new String[]{itemTitle});
    }

    public Cursor getAllData() {
        String buildSQL = "SELECT * FROM " + GeekNotesContract.GeekEntry.TABLE_NAME + " ORDER BY "
                + GeekNotesContract.GeekEntry._ID + " DESC";
        return database.rawQuery(buildSQL, null);
    }

    public Cursor getSpecificNote(String title) {
        String buildSQL = "SELECT * FROM " + GeekNotesContract.GeekEntry.TABLE_NAME + " WHERE " +
                GeekNotesContract.GeekEntry.COLUMN_TITLE + " = ?";
        return database.rawQuery(buildSQL, new String[] {title});
    }

    public Cursor getItemsByCategory(String categoryName) {
        String buildSQL = "SELECT * FROM " + GeekNotesContract.GeekEntry.TABLE_NAME + " WHERE TRIM("
                + GeekNotesContract.GeekEntry.COLUMN_CATEGORY + ") = '" + categoryName.trim() + "' ORDER BY " + GeekNotesContract.GeekEntry._ID + " DESC";
        return database.rawQuery(buildSQL, null);
    }

    public void deleteByTitle(String title) {
        String[] whereArgs = new String[]{title};
        database.delete(GeekNotesContract.GeekEntry.TABLE_NAME, GeekNotesContract.GeekEntry.COLUMN_TITLE + " = ?", whereArgs);
    }

    public class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            final String SQL_CREATE_NOTES_TABLE = "CREATE TABLE " + GeekNotesContract.GeekEntry.TABLE_NAME + " (" +
                    GeekNotesContract.GeekEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                    GeekNotesContract.GeekEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    GeekNotesContract.GeekEntry.COLUMN_CATEGORY + " INTEGER NOT NULL, " +
                    GeekNotesContract.GeekEntry.COLUMN_INFOTYPE + " TEXT, " +
                    GeekNotesContract.GeekEntry.COLUMN_INFO + " TEXT, " +
                    GeekNotesContract.GeekEntry.COLUMN_ARTICLE_INFO + " TEXT, " +
                    GeekNotesContract.GeekEntry.COLUMN_TRANSLATION + " TEXT, " +
                    GeekNotesContract.GeekEntry.COLUMN_ARTICLE_RANK + " REAL, " +
                    GeekNotesContract.GeekEntry.COLUMN_ARTICLE_IMDB_INFO + " TEXT, " +
                    GeekNotesContract.GeekEntry.COLUMN_ARTICLE_POSTERLINK + " TEXT);";

            database.execSQL(SQL_CREATE_NOTES_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("DROP TABLE IF EXISTS " + GeekNotesContract.GeekEntry.TABLE_NAME);
            onCreate(database);
        }
    }
}
