package com.example.user.movieproject.controller;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.user.movieproject.R;
import com.example.user.movieproject.data.MovieContract;

public class DetailActivity extends AppCompatActivity {
    String LOG_TAG = DetailActivity.class.getSimpleName();
    String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.movie_detail_container, new DetailFragment())
                    .commit();
        }
        Intent intent = getIntent();
        Uri movieUriWithId = intent.getData();
        movieId = Utility.getMovieIdFromUri(this, movieUriWithId);

    }

    public void addListenerToFavButtton(View view) {
        Log.d(LOG_TAG, "Adding to favourite table");
        ContentValues contentValues = new ContentValues();
        Cursor query_movie;
        int update_movie;

        // Checking sort preference
        if (Utility.getSortPreference(getApplicationContext()).equals("0")) {

            // Querying data from Most_Pop and adding it to a ContentValues
            query_movie = getContentResolver().query(
                    MovieContract.MostPopMovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MostPopMovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{movieId},
                    null);
            if (query_movie != null && query_movie.moveToFirst()) {
                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID,
                        query_movie.getString(query_movie.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_MOVIE_ID)));
                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_TITLE,
                        query_movie.getString(query_movie.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_TITLE)));
                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_PLOT,
                        query_movie.getString(query_movie.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_PLOT)));
                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH,
                        query_movie.getString(query_movie.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_POSTER_PATH)));
                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_VOTE_AVG,
                        query_movie.getString(query_movie.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_VOTE_AVG)));
                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_RELEASE_DATE,
                        query_movie.getString(query_movie.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_RELEASE_DATE)));
            }

            // Updating is_favourite column in Most_Pop table
            ContentValues cv = new ContentValues();
            cv.put(MovieContract.MostPopMovieEntry.COLUMN_IS_FAVOURITE, 1);
            int _ID = query_movie.getInt(query_movie.getColumnIndex(MovieContract.MostPopMovieEntry._ID));
            update_movie = getContentResolver().update(
                    MovieContract.MostPopMovieEntry.CONTENT_URI,
                    cv,
                    MovieContract.MostPopMovieEntry._ID + " = ?",
                    new String[]{String.valueOf(_ID)});
            Log.d(LOG_TAG, "" + update_movie);
        } else {

            //Querying data from Top_Rated and adding it to a ContentValues

            query_movie = getContentResolver().query(
                    MovieContract.TopRatedMovieEntry.CONTENT_URI,
                    null,
                    MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{movieId},
                    null);
            if (query_movie != null && query_movie.moveToFirst()) {

                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID,
                        query_movie.getString(query_movie.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_ID)));
                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_TITLE,
                        query_movie.getString(query_movie.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_TITLE)));
                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_PLOT,
                        query_movie.getString(query_movie.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_PLOT)));
                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH,
                        query_movie.getString(query_movie.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_POSTER_PATH)));
                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_VOTE_AVG,
                        query_movie.getString(query_movie.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_VOTE_AVG)));
                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_RELEASE_DATE,
                        query_movie.getString(query_movie.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_RELEASE_DATE)));
            }

            // Updating is_favourite column in Top_Rated table
            ContentValues cv = new ContentValues();
            int _ID = query_movie.getInt(query_movie.getColumnIndex(MovieContract.TopRatedMovieEntry._ID));
            cv.put(MovieContract.TopRatedMovieEntry.COLUMN_IS_FAVOURITE, 1);
            update_movie = getContentResolver().update(
                    MovieContract.TopRatedMovieEntry.CONTENT_URI,
                    cv,
                    MovieContract.TopRatedMovieEntry._ID + " = ?",
                    new String[]{String.valueOf(_ID)});
            Log.d(LOG_TAG, "" + update_movie);
        }
        query_movie.close();
        getContentResolver().insert(MovieContract.FavouriteMoviesEntry.CONTENT_URI, contentValues);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        else if (id == R.id.home) {
            this.finish();
            return true;
        }
        else if (id == R.id.favPage){
            Intent intent = new Intent(this, FavouriteActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
