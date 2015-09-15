package com.example.user.movieproject.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by USER on 9/9/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + com.example.user.movieproject.model.MovieContract.MovieEntry.TABLE_NAME + "( "+
                com.example.user.movieproject.model.MovieContract.MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                com.example.user.movieproject.model.MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                com.example.user.movieproject.model.MovieContract.MovieEntry.COLUMN_PLOT + " TEXT NOT NULL," +
                com.example.user.movieproject.model.MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                com.example.user.movieproject.model.MovieContract.MovieEntry.COLUMN_VOTE_AVG + " REAL NOT NULL," +
                com.example.user.movieproject.model.MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL" + ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF IT EXISTS "+ com.example.user.movieproject.model.MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}