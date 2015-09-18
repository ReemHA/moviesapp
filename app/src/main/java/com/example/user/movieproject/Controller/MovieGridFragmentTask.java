package com.example.user.movieproject.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.user.movieproject.model.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieGridFragmentTask extends AsyncTask<String, Void, Void> {
    private final Context mContext;

    public MovieGridFragmentTask(Context context) {
        mContext = context;
    }

    void addMovie(String id, String title, String release_date, Double voting, String poster_path, String plot){
        long rowId;
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry._ID},
                MovieContract.MovieEntry.COLUMN_ID + " = ?",
                new String[]{id},
                null);
        if (!movieCursor.moveToFirst()){
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_ID, id);
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, release_date);
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, voting);
            contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, poster_path);
            contentValues.put(MovieContract.MovieEntry.COLUMN_PLOT, plot);

            Uri uri = mContext.getContentResolver().insert(
                    MovieContract.MovieEntry.CONTENT_URI,
                    contentValues
            );
        }else{
            int movieIdIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry._ID);
        }
        movieCursor.close();
        return;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Void doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }
        String movieQuery = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;
        String format = "json";
        String mId = "";
        try {

            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?sort_by=?";
            final String SORT_PREF = "";
            final String FORMAT_PARAM = "";
            final String MOVIE_ID = "";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(MOVIE_ID, mId)
                    .appendQueryParameter(SORT_PREF, params[0])
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            movieJsonStr = buffer.toString();
            getMovieDataFromJson(movieJsonStr, movieQuery);
        } catch (IOException e) {
            Log.e("LOC ERROR", "Error ", e);
        } catch (JSONException e) {
            Log.e("LOC ERROR", e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("LOC ERROR", "Error closing stream", e);
                }
            }
        }
        return null;
    }

    private void getMovieDataFromJson(String jsonStream, String movieQuery) throws JSONException {
        final String LIST = "results";
        final String ID = "id";
        final String TITLE = "title";
        final String RELEASE_DATE = "release_date";
        final String RATING = "vote_average";
        final String OVERVIEW = "lon";
        final String POSTER = "poster_path";

        JSONObject movieJson = new JSONObject(jsonStream);
        JSONArray listMovies = movieJson.getJSONArray(LIST);

        for (int i = 0; i < listMovies.length(); i++) {
            JSONObject movie = listMovies.getJSONObject(i);
            String movieID = movie.getString(ID);
            String title = movie.getString(TITLE);
            double vote_average = movie.getDouble(RATING);
            String release_date = movie.getString(RELEASE_DATE);
            String plot = movie.getString(OVERVIEW);
            String poster_path = "http://image.tmdb.org/t/p/w185/" + movie.getString(POSTER);
            addMovie(movieID, title, release_date, vote_average, poster_path, plot);
        }
        return;
    }
}
