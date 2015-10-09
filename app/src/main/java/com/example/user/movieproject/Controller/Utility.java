package com.example.user.movieproject.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.user.movieproject.R;
import com.example.user.movieproject.data.MovieContract;

/**
 * Created by USER on 10/3/2015.
 */
public class Utility {
    public static String getSortPreference(Context context){
        String storedPreferences;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        storedPreferences = preferences.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default));
        return storedPreferences;
    }

    public static String getMovieIdFromUri(Context context, Uri uri) {
        Cursor cursor;
        if (Utility.getSortPreference(context).equals("0")) {
            long _id = MovieContract.MostPopMovieEntry.getIdFromUri(uri);
            cursor = context.getContentResolver().query(MovieContract.MostPopMovieEntry.CONTENT_URI,
                    new String[]{MovieContract.MostPopMovieEntry._ID, MovieContract.MostPopMovieEntry.COLUMN_MOVIE_ID},
                    MovieContract.MostPopMovieEntry._ID + " = ?",
                    new String[]{String.valueOf(_id)},
                    null);
            if (cursor.moveToFirst()){
                int colIndex = cursor.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_MOVIE_ID);
                return cursor.getString(colIndex);
            }else {
                return null;
            }
        } else {
            long _id = MovieContract.TopRatedMovieEntry.getIdFromUri(uri);
            cursor = context.getContentResolver().query(MovieContract.TopRatedMovieEntry.CONTENT_URI,
                    new String[]{MovieContract.TopRatedMovieEntry._ID, MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_ID},
                    MovieContract.TopRatedMovieEntry._ID + " = ?",
                    new String[]{String.valueOf(_id)},
                    null);
            if (cursor.moveToFirst()){
                int colIndex = cursor.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_ID);
                return cursor.getString(colIndex);
            }else {
                return null;
            }
        }
    }
}
