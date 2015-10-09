package com.example.user.movieproject.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.user.movieproject.controller.Utility;

/**
 * Created by USER on 9/9/2015.
 */
public class MovieDbProvider extends ContentProvider {

    private MovieDbHelper movieDbHelper;
    static String LOG_TAG = MovieDbProvider.class.getSimpleName();
    private static final int TOP_RATED_MOVIE = 100;
    private static final int TOP_RATED_MOVIE_WITH_ID = 101;
    private static final int TOP_RATED_MOVIE_WITH_POSTER = 102;

    private static final int MOST_POP_MOVIE = 200;
    private static final int MOST_POP_MOVIE_WITH_ID = 201;
    private static final int MOST_POP_MOVIE_WITH_POSTER = 202;

    private static final int FAV_MOVIE = 300;
    private static final int FAV_MOVIE_WITH_ID = 301;
    private static final int FAV_MOVIE_WITH_POSTER = 302;

    private static final int TRAILER_MOVIE = 400;
    private static final int TRAILER_MOVIE_WITH_ID = 401;

    private static final int REV_MOVIE = 500;
    private static final int REV_MOVIE_WITH_ID = 501;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_TOP_RATED_MOVIES,
                TOP_RATED_MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_TOP_RATED_MOVIES + "/#",
                TOP_RATED_MOVIE_WITH_ID);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_TOP_RATED_MOVIES + "/*",
                TOP_RATED_MOVIE_WITH_POSTER);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOST_POP_MOVIE,
                MOST_POP_MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOST_POP_MOVIE + "/#",
                MOST_POP_MOVIE_WITH_ID);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOST_POP_MOVIE + "/*",
                MOST_POP_MOVIE_WITH_POSTER);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAV_MOVIE,
                FAV_MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAV_MOVIE + "/#",
                FAV_MOVIE_WITH_ID);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAV_MOVIE + "/*",
                FAV_MOVIE_WITH_POSTER);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_TRAILER,
                TRAILER_MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_TRAILER + "/#",
                TRAILER_MOVIE_WITH_ID);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_REVIEWS,
                REV_MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_REVIEWS + "/#",
                REV_MOVIE_WITH_ID);
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case TOP_RATED_MOVIE: {
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.TopRatedMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TOP_RATED_MOVIE_WITH_ID:
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.TopRatedMovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.TopRatedMovieEntry._ID + " = ?",
                        new String[]{Long.toString(MovieContract.TopRatedMovieEntry.getIdFromUri(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            case TOP_RATED_MOVIE_WITH_POSTER:
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.TopRatedMovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.TopRatedMovieEntry.COLUMN_POSTER_PATH + " = ?",
                        new String[]{MovieContract.TopRatedMovieEntry.getPosterUrlFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOST_POP_MOVIE:
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.MostPopMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOST_POP_MOVIE_WITH_ID:
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.MostPopMovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MostPopMovieEntry._ID + " = ?",
                        new String[]{Long.toString(MovieContract.MostPopMovieEntry.getIdFromUri(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;

            case MOST_POP_MOVIE_WITH_POSTER:
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.MostPopMovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MostPopMovieEntry.COLUMN_POSTER_PATH + " = ?",
                        new String[]{MovieContract.MostPopMovieEntry.getPosterUrlFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAV_MOVIE:
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.FavouriteMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAV_MOVIE_WITH_ID:
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.FavouriteMoviesEntry.TABLE_NAME,
                        projection,
                        MovieContract.FavouriteMoviesEntry._ID + " = ?",
                        new String[]{Long.toString(MovieContract.FavouriteMoviesEntry.getIdFromUri(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRAILER_MOVIE:
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.TrailersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRAILER_MOVIE_WITH_ID:
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.TrailersEntry.TABLE_NAME,
                        projection,
                        MovieContract.TrailersEntry.COLUMN_MOVIE_ID+ " = ?",
                        new String[]{Utility.getMovieIdFromUri(getContext(), uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case REV_MOVIE:
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.ReviewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REV_MOVIE_WITH_ID:
                cursor = movieDbHelper.getReadableDatabase().query(MovieContract.ReviewsEntry.TABLE_NAME,
                        projection,
                        MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{Long.toString(MovieContract.ReviewsEntry.getMovieIdFromUri(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
        }
        String a;
        if (cursor == null) {
            a = "Cursor returned from query is null";
        } else {
            a = "Cursor returned from query is not null";
        }
        Log.d(LOG_TAG, a);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TOP_RATED_MOVIE:
                return MovieContract.TopRatedMovieEntry.CONTENT_TYPE;

            case TOP_RATED_MOVIE_WITH_ID:
                return MovieContract.TopRatedMovieEntry.CONTENT_ITEM_TYPE;

            case TOP_RATED_MOVIE_WITH_POSTER:
                return MovieContract.TopRatedMovieEntry.CONTENT_ITEM_TYPE;

            case MOST_POP_MOVIE:
                return MovieContract.MostPopMovieEntry.CONTENT_TYPE;

            case MOST_POP_MOVIE_WITH_ID:
                return MovieContract.MostPopMovieEntry.CONTENT_ITEM_TYPE;

            case MOST_POP_MOVIE_WITH_POSTER:
                return MovieContract.MostPopMovieEntry.CONTENT_ITEM_TYPE;

            case FAV_MOVIE:
                return MovieContract.FavouriteMoviesEntry.CONTENT_TYPE;

            case FAV_MOVIE_WITH_ID:
                return MovieContract.FavouriteMoviesEntry.CONTENT_ITEM_TYPE;

            case FAV_MOVIE_WITH_POSTER:
                return MovieContract.FavouriteMoviesEntry.CONTENT_ITEM_TYPE;

            case TRAILER_MOVIE:
                return MovieContract.TrailersEntry.CONTENT_TYPE;

            case TRAILER_MOVIE_WITH_ID:
                return MovieContract.TrailersEntry.CONTENT_ITEM_TYPE;

            case REV_MOVIE:
                return MovieContract.ReviewsEntry.CONTENT_TYPE;

            case REV_MOVIE_WITH_ID:
                return MovieContract.ReviewsEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uriMatcher.match(uri));
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri;
        if (uriMatcher.match(uri) != FAV_MOVIE &&
                uriMatcher.match(uri) != MOST_POP_MOVIE &&
                uriMatcher.match(uri) != TOP_RATED_MOVIE &&
                uriMatcher.match(uri) != TRAILER_MOVIE &&
                uriMatcher.match(uri) != REV_MOVIE) {
            throw new IllegalArgumentException("Unsupported URI for insertion: " + uri);
        }
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case FAV_MOVIE: {
                long id = db.insert(MovieContract.FavouriteMoviesEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = MovieContract.FavouriteMoviesEntry.buildMovieWithId(id);
                } else {
                    throw new SQLException("Error in insertion: ");
                }
                break;
            }
            case MOST_POP_MOVIE: {
                long id = db.insert(MovieContract.MostPopMovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = MovieContract.MostPopMovieEntry.buildMovieWithId(id);
                } else {
                    throw new SQLException("Error in insertion: ");
                }
                break;
            }
            case TOP_RATED_MOVIE: {
                long id = db.insert(MovieContract.TopRatedMovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = MovieContract.TopRatedMovieEntry.buildMovieWithId(id);
                } else {
                    throw new SQLException("Error in insertion: ");
                }
                break;
            }
            case TRAILER_MOVIE: {
                long id = db.insert(MovieContract.TrailersEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = MovieContract.TrailersEntry.buildTrailerUri(id);
                } else {
                    throw new SQLException("Error in insertion: ");
                }
                break;
            }
            case REV_MOVIE: {
                long id = db.insert(MovieContract.ReviewsEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = MovieContract.ReviewsEntry.buildReviewUri(id);
                } else {
                    throw new SQLException("Error in insertion: ");
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unsupported uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsDeleted;
        if (selection == null) {
            selection = "1";
        }
        switch (match) {
            case MOST_POP_MOVIE:
                rowsDeleted = db.delete(MovieContract.MostPopMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case TOP_RATED_MOVIE:
                rowsDeleted = db.delete(MovieContract.TopRatedMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case TRAILER_MOVIE:
                rowsDeleted = db.delete(MovieContract.TrailersEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case REV_MOVIE:
                rowsDeleted = db.delete(MovieContract.ReviewsEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case FAV_MOVIE:
                rowsDeleted = db.delete(MovieContract.FavouriteMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case MOST_POP_MOVIE:
                rowsUpdated = db.update(MovieContract.MostPopMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case TOP_RATED_MOVIE:
                rowsUpdated = db.update(MovieContract.TopRatedMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case TRAILER_MOVIE:
                rowsUpdated = db.update(MovieContract.TrailersEntry.TABLE_NAME, values,selection, selectionArgs);
                break;

            case REV_MOVIE:
                rowsUpdated = db.update(MovieContract.ReviewsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;

            case FAV_MOVIE:
                rowsUpdated = db.update(MovieContract.FavouriteMoviesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return rowsUpdated;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case FAV_MOVIE: {
                int count_t = 0;
                try {
                    db.beginTransaction();
                    for (ContentValues c : values) {
                        long id = db.insert(MovieContract.FavouriteMoviesEntry.TABLE_NAME, null, c);
                        if (id != -1) {
                            count_t++;
                        }
                    }
                    db.setTransactionSuccessful();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count_t;
            }
            case MOST_POP_MOVIE: {
                int count_t = 0;
                try {
                    db.beginTransaction();
                    for (ContentValues c : values) {
                        long id = db.insert(MovieContract.MostPopMovieEntry.TABLE_NAME, null, c);
                        if (id != -1) {
                            count_t++;
                        }
                    }
                    db.setTransactionSuccessful();
                } catch (IllegalStateException E) {
                    E.printStackTrace();
                } finally {
                    db.endTransaction();
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count_t;
            }
            case TOP_RATED_MOVIE: {
                int count_t = 0;
                try {
                    db.beginTransaction();
                    for (ContentValues c : values) {
                        long id = db.insert(MovieContract.TopRatedMovieEntry.TABLE_NAME, null, c);
                        if (id != -1) {
                            count_t++;
                        }
                    }
                    db.setTransactionSuccessful();
                } catch (IllegalStateException E) {
                    E.printStackTrace();
                } finally {
                    db.endTransaction();
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count_t;
            }
            case TRAILER_MOVIE: {
                int count_t = 0;
                try {
                    db.beginTransaction();
                    for (ContentValues c : values) {
                        long id = db.insert(MovieContract.TrailersEntry.TABLE_NAME, null, c);
                        if (id != -1) {
                            count_t++;
                        }
                    }
                    db.setTransactionSuccessful();
                } catch (IllegalStateException E) {
                    E.printStackTrace();
                } finally {
                    db.endTransaction();
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count_t;
            }
            case REV_MOVIE: {
                int count_t = 0;
                try {
                    db.beginTransaction();
                    for (ContentValues c : values) {
                        long id = db.insert(MovieContract.ReviewsEntry.TABLE_NAME, null, c);
                        if (id != -1) {
                            count_t++;
                        }
                    }
                    db.setTransactionSuccessful();
                } catch (IllegalStateException E) {
                    E.printStackTrace();
                } finally {
                    db.endTransaction();
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count_t;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
