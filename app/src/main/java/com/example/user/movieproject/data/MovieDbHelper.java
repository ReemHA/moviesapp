package com.example.user.movieproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by USER on 9/9/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TOP_RATED_MOVIE_TABLE = "CREATE TABLE " + MovieContract.TopRatedMovieEntry.TABLE_NAME + "( "
                + MovieContract.TopRatedMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_ID + " INT NOT NULL, " +
                MovieContract.TopRatedMovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                MovieContract.TopRatedMovieEntry.COLUMN_PLOT + " TEXT NOT NULL," +
                MovieContract.TopRatedMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                MovieContract.TopRatedMovieEntry.COLUMN_VOTE_AVG + " REAL NOT NULL," +
                MovieContract.TopRatedMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                MovieContract.TopRatedMovieEntry.COLUMN_IS_FAVOURITE + " INT);";
        final String SQL_CREATE_MOST_POP_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MostPopMovieEntry.TABLE_NAME + "( "
                + MovieContract.MostPopMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MostPopMovieEntry.COLUMN_MOVIE_ID + " INT NOT NULL, " +
                MovieContract.MostPopMovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                MovieContract.MostPopMovieEntry.COLUMN_PLOT + " TEXT NOT NULL," +
                MovieContract.MostPopMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                MovieContract.MostPopMovieEntry.COLUMN_VOTE_AVG + " REAL NOT NULL," +
                MovieContract.MostPopMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                MovieContract.MostPopMovieEntry.COLUMN_IS_FAVOURITE + " INT NOT NULL);";

        final String SQL_CREATE_FAV_MOVIE_TABLE = "CREATE TABLE " + MovieContract.FavouriteMoviesEntry.TABLE_NAME + "( "
                + MovieContract.FavouriteMoviesEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID + " INT NOT NULL, " +
                MovieContract.FavouriteMoviesEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                MovieContract.FavouriteMoviesEntry.COLUMN_PLOT + " TEXT NOT NULL," +
                MovieContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                MovieContract.FavouriteMoviesEntry.COLUMN_VOTE_AVG + " REAL NOT NULL," +
                MovieContract.FavouriteMoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL);";
        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MovieContract.TrailersEntry.TABLE_NAME + "( "
                + MovieContract.TrailersEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.TrailersEntry.COLUMN_TRAILER_ID + " TEXT NOT NULL, " +
                MovieContract.TrailersEntry.COLUMN_MOVIE_ID + " INT NOT NULL," +
                MovieContract.TrailersEntry.COLUMN_YOUTUBE_KEY + " TEXT NOT NULL," +
                MovieContract.TrailersEntry.COLUMN_NAME + " TEXT NOT NULL);";
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.ReviewsEntry.TABLE_NAME + "( "
                + MovieContract.ReviewsEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.ReviewsEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL, " +
                MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + " INT NOT NULL," +
                MovieContract.ReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL," +
                MovieContract.ReviewsEntry.COLUMN_TEXT + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_TOP_RATED_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_MOST_POP_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_FAV_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TopRatedMovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MostPopMovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavouriteMoviesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailersEntry.TABLE_NAME);
        onCreate(db);
    }
}
