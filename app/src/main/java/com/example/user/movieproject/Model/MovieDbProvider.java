package com.example.user.movieproject.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by USER on 9/9/2015.
 */
public class MovieDbProvider extends ContentProvider {
    private SQLiteDatabase db;
    private com.example.user.movieproject.model.MovieDbHelper movieDbHelper;
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.MovieEntry.TABLE_NAME,
                MovieContract.MovieEntry.MOVIE_LIST);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.MovieEntry.TABLE_NAME + "#",
                MovieContract.MovieEntry.MOVIE_LIST);    }
    @Override
    public boolean onCreate() {
        boolean isCreated;
        movieDbHelper = new com.example.user.movieproject.model.MovieDbHelper(getContext());
        db = movieDbHelper.getReadableDatabase();
        if (db == null) {
            isCreated = false;
            return isCreated;
        }
        isCreated = true;
        return isCreated;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
