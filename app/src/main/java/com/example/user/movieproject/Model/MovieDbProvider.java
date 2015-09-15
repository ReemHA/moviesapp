package com.example.user.movieproject.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by USER on 9/9/2015.
 */
public class MovieDbProvider extends ContentProvider {
    private SQLiteDatabase db;
    private MovieDbHelper movieDbHelper;
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.MovieEntry.TABLE_NAME,
                MovieContract.MovieEntry.MOVIE_LIST);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.MovieEntry.TABLE_NAME + "/#",
                MovieContract.MovieEntry.MOVIE_ID);    }
    @Override
    public boolean onCreate() {
        boolean isCreated;
        movieDbHelper = new MovieDbHelper(getContext());
        db = movieDbHelper.getReadableDatabase();
        if (db == null) {
            isCreated = false;
            return isCreated;
        }
        isCreated = true;
        return isCreated;
    }

    synchronized
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = movieDbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        boolean useAuthorityUri = false;


        return null;
    }

    synchronized
    @Override
    public String getType(Uri uri) {
    switch (uriMatcher.match(uri)){
        case MovieContract.MovieEntry.MOVIE_LIST:
            return MovieContract.MovieEntry.CONTENT_TYPE;
        case MovieContract.MovieEntry.MOVIE_ID:
            return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
        default:
        return null;
        }
    }

    synchronized
    @Override
    public Uri insert(Uri uri, ContentValues values) throws UnsupportedOperationException{
        return null;
    }

    synchronized
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) throws UnsupportedOperationException{
        return 0;
    }

    synchronized
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) throws UnsupportedOperationException{
        return 0;
    }
}
