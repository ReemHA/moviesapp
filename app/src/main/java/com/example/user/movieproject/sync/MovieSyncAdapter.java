package com.example.user.movieproject.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.user.movieproject.R;
import com.example.user.movieproject.controller.Utility;
import com.example.user.movieproject.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by USER on 10/3/2015.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    private final String API_KEY = "0bed95c67895bbde6f8d00e7e464c50a";
    public final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    static String movieURL;
    String sortPreference = Utility.getSortPreference(getContext());

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        fetchMovieDescData();
        return;
    }


    private void fetchMovieDescData() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String format = "json";
        String movieJsonStr;
        String sort_by_pop = "popularity.desc";
        String sort_by_top = "vote_average.desc";
        Uri builtUri;
        try {
            if (sortPreference.equals("0")) {
                movieURL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=?";
                builtUri = Uri.parse(movieURL).buildUpon()
                        .appendQueryParameter("api_key", API_KEY).build();
            } else {
                movieURL = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=?";
                builtUri = Uri.parse(movieURL).buildUpon()
                        .appendQueryParameter("api_key", API_KEY).build();
            }
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return;
            }
            movieJsonStr = buffer.toString();
            getMovieDataFromJson(movieJsonStr, sortPreference);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    private void fetchTrailers(String url_trailer, String movieId) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String format = "json";
        String trailerJsonStr;
        Uri builtUri;
        try {
            builtUri = Uri.parse(url_trailer).buildUpon().build();
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return;
            }
            trailerJsonStr = buffer.toString();
            addToTrailerTable(trailerJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }


    private void fetchReviews(String url_review, String movieId) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String format = "json";
        String reviewsJsonStr;
        Uri builtUri;
        try {
            builtUri = Uri.parse(url_review).buildUpon().build();
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return;
            }
            reviewsJsonStr = buffer.toString();
            addToReviewTable(reviewsJsonStr, movieId);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    private void addToTrailerTable(String stream) throws JSONException {
        JSONObject ObjectsOfVideos = new JSONObject(stream);
        JSONArray listVideos = ObjectsOfVideos.getJSONArray("results");
        String movieId = ObjectsOfVideos.getString("id");
        ContentValues contentValues = new ContentValues();
        Vector<ContentValues> cVV = new Vector<>(listVideos.length());
        for (int i = 1; i < listVideos.length(); i++) {
            JSONObject video = listVideos.getJSONObject(i);
            contentValues.put(MovieContract.TrailersEntry.COLUMN_YOUTUBE_KEY, video.getString("key"));
            contentValues.put(MovieContract.TrailersEntry.COLUMN_NAME, video.getString("name"));
            contentValues.put(MovieContract.TrailersEntry.COLUMN_MOVIE_ID, movieId);
            contentValues.put(MovieContract.TrailersEntry.COLUMN_TRAILER_ID, video.getString("id"));
            cVV.add(contentValues);
        }
        if (cVV.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVV.size()];
            cVV.toArray(cvArray);
            getContext().getContentResolver().delete(MovieContract.TrailersEntry.CONTENT_URI, null, null);
            getContext().getContentResolver().bulkInsert(MovieContract.TrailersEntry.CONTENT_URI, cvArray);
        }
        return;
    }


    private void addToReviewTable(String stream, String movieID) throws JSONException {
        Log.d("location", "inGetReviews");
        JSONObject ObjectsOfTweets = new JSONObject(stream);
        JSONArray listReviews = ObjectsOfTweets.getJSONArray("results");
        ContentValues contentValues = new ContentValues();
        Vector<ContentValues> cVV = new Vector<>(listReviews.length());
        for (int i = 0; i < listReviews.length(); i++) {
            JSONObject review = listReviews.getJSONObject(i);
            contentValues.put(MovieContract.ReviewsEntry.COLUMN_AUTHOR, review.getString("author"));
            contentValues.put(MovieContract.ReviewsEntry.COLUMN_TEXT, review.getString("content"));
            contentValues.put(MovieContract.ReviewsEntry.COLUMN_REVIEW_ID, review.getString("id"));
            contentValues.put(MovieContract.ReviewsEntry.COLUMN_MOVIE_ID, movieID);
            cVV.add(contentValues);
        }
        if (cVV.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVV.size()];
            cVV.toArray(cvArray);
            getContext().getContentResolver().delete(MovieContract.ReviewsEntry.CONTENT_URI, null, null);
            getContext().getContentResolver().bulkInsert(MovieContract.ReviewsEntry.CONTENT_URI, cvArray);
            Log.d(LOG_TAG, "Sync Complete. " + cVV.size() + " Inserted");
        }
        return;
    }

    public void getMovieDataFromJson(String stream, String sortPref) throws JSONException {
        try {
            JSONObject ObjectsOfMovies = new JSONObject(stream);
            JSONArray listMovies = ObjectsOfMovies.getJSONArray("results");
            Vector<ContentValues> contentVVMostPop = new Vector<>(listMovies.length());
            Vector<ContentValues> contentVVTopRated = new Vector<>(listMovies.length());

            for (int i = 0; i < listMovies.length(); i++) {
                JSONObject movie = listMovies.getJSONObject(i);
                String videoUrl = "http://api.themoviedb.org/3/movie/" + movie.getString("id") + "/videos" + "?api_key=" + API_KEY;
                String reviewUrl = "http://api.themoviedb.org/3/movie/" + movie.getString("id") + "/reviews" + "?api_key=" + API_KEY;
                fetchTrailers(videoUrl, movie.getString("id"));
                fetchReviews(reviewUrl, movie.getString("id"));
                ContentValues movieValues = new ContentValues();
                if (sortPref.equals("0")) {
                    movieValues.put(MovieContract.MostPopMovieEntry.COLUMN_MOVIE_ID, movie.getString("id"));
                    movieValues.put(MovieContract.MostPopMovieEntry.COLUMN_TITLE, movie.getString("title"));
                    movieValues.put(MovieContract.MostPopMovieEntry.COLUMN_VOTE_AVG, movie.getDouble("vote_average"));
                    movieValues.put(MovieContract.MostPopMovieEntry.COLUMN_RELEASE_DATE, movie.getString("release_date").split("-")[0]);
                    movieValues.put(MovieContract.MostPopMovieEntry.COLUMN_PLOT, movie.getString("overview"));
                    movieValues.put(MovieContract.MostPopMovieEntry.COLUMN_POSTER_PATH, "http://image.tmdb.org/t/p/w185/" + movie.getString("poster_path"));
                    movieValues.put(MovieContract.MostPopMovieEntry.COLUMN_IS_FAVOURITE, 0);
                    contentVVMostPop.add(movieValues);

                } else {
                    movieValues.put(MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_ID, movie.getString("id"));
                    movieValues.put(MovieContract.TopRatedMovieEntry.COLUMN_TITLE, movie.getString("title"));
                    movieValues.put(MovieContract.TopRatedMovieEntry.COLUMN_VOTE_AVG, movie.getDouble("vote_average"));
                    movieValues.put(MovieContract.TopRatedMovieEntry.COLUMN_RELEASE_DATE, movie.getString("release_date").split("-")[0]);
                    movieValues.put(MovieContract.TopRatedMovieEntry.COLUMN_PLOT, movie.getString("overview"));
                    movieValues.put(MovieContract.TopRatedMovieEntry.COLUMN_POSTER_PATH, "http://image.tmdb.org/t/p/w185/" + movie.getString("poster_path"));
                    movieValues.put(MovieContract.TopRatedMovieEntry.COLUMN_IS_FAVOURITE, 0);
                    contentVVTopRated.add(movieValues);

                }
            }

            if (contentVVMostPop.size() > 0) {
                ContentValues[] cvArray = new ContentValues[contentVVMostPop.size()];
                contentVVMostPop.toArray(cvArray);
                getContext().getContentResolver().delete(MovieContract.MostPopMovieEntry.CONTENT_URI, null, null);
                getContext().getContentResolver().bulkInsert(MovieContract.MostPopMovieEntry.CONTENT_URI, cvArray);
                Log.d(LOG_TAG, "Sync complete and " + contentVVMostPop.size() + " records inserted");

            }
            if (contentVVTopRated.size() > 0) {
                ContentValues[] cvArray = new ContentValues[contentVVTopRated.size()];
                contentVVTopRated.toArray(cvArray);
                getContext().getContentResolver().delete(MovieContract.TopRatedMovieEntry.CONTENT_URI, null, null);
                getContext().getContentResolver().bulkInsert(MovieContract.TopRatedMovieEntry.CONTENT_URI, cvArray);
                Log.d(LOG_TAG, "Sync complete and " + contentVVTopRated.size() + " records inserted");
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}


