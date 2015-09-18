package com.example.user.movieproject.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by USER on 9/9/2015.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.user.movieproject";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ALL_MOVIES = "all_movies";
    public static final String PATH_TOP_RATED_MOVIES = "top_rated_movies";
    public static final String PATH_MOST_POP_MOVIE = "most_pop_movies";

    public static class MovieEntry implements BaseColumns {
        public static int MOVIE_LIST = 1;
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ALL_MOVIES).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALL_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALL_MOVIES;

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PLOT = "overview";
        public static final String COLUMN_VOTE_AVG = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "http://image.tmdb.org/t/p/w185/" + "poster_path";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }
    public static final class TopRatedMovieEntry implements BaseColumns {
        public static int MOVIE_LIST = 2;
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOP_RATED_MOVIES).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED_MOVIES;

        public static final String TABLE_NAME = "top_rated_movie";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PLOT = "overview";
        public static final String COLUMN_VOTE_AVG = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "http://image.tmdb.org/t/p/w185/" + "poster_path";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildTopRatedMoviesUri(String pref){
            return CONTENT_URI.buildUpon().appendPath(pref).build();
        }
    }
    public static final class MostPopMovieEntry implements BaseColumns{
        public static int MOVIE_LIST = 3;
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOST_POP_MOVIE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOST_POP_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOST_POP_MOVIE;

        public static final String TABLE_NAME = "most_pop_movies";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PLOT = "overview";
        public static final String COLUMN_VOTE_AVG = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "http://image.tmdb.org/t/p/w185/" + "poster_path";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMostPopMoviesUri(String pref){
            return CONTENT_URI.buildUpon().appendPath(pref).build();
        }


    }



}
