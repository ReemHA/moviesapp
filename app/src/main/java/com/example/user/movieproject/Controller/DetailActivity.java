package com.example.user.movieproject.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.user.movieproject.R;
import com.example.user.movieproject.data.MovieContract;
import com.example.user.movieproject.data.MovieDbHelper;

public class DetailActivity extends AppCompatActivity {
    String LOG_TAG = DetailActivity.class.getSimpleName();
    String movieId;
    private static int is_favourite = 0;
    private static Uri uri;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState != null) {
            return;
        }

        Bundle arguments = new Bundle();
        arguments.putParcelable(DetailFragment.MOVIE_URI_WITH_ID, getIntent().getData());
        arguments.putString("className", LOG_TAG);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(arguments);
        getFragmentManager().beginTransaction().add(R.id.movie_detail_container, detailFragment).commit();

        uri = arguments.getParcelable(DetailFragment.MOVIE_URI_WITH_ID);
        movieId = Utility.getMovieIdFromUri(this, uri);

    }

    public void addListenerToFavButtton(View view) {
        ContentValues contentValues = new ContentValues();
        Cursor query_movie;
        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        // Checking sort preference
        if (Utility.getSortPreference(this).equals("0")) {

            // Querying data from Most_Pop and adding it to a ContentValues
            query_movie = this.getContentResolver().query(
                    MovieContract.MostPopMovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MostPopMovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{movieId},
                    null);
            if (query_movie != null && query_movie.moveToFirst()) {
                contentValues.put(MovieContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID,
                        query_movie.getInt(query_movie.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_MOVIE_ID)));
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
                is_favourite = query_movie.getInt(query_movie.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_IS_FAVOURITE));

                if (is_favourite == 0) {
                    // Updating is_favourite column in Most_Pop table by 1
                    db.execSQL("UPDATE " + MovieContract.MostPopMovieEntry.TABLE_NAME + " SET " +
                            MovieContract.MostPopMovieEntry.COLUMN_IS_FAVOURITE + " = '1' WHERE "
                            + MovieContract.MostPopMovieEntry._ID + " = "
                            + query_movie.getInt(query_movie.getColumnIndex(MovieContract.MostPopMovieEntry._ID)));

                    getContentResolver().insert(MovieContract.FavouriteMoviesEntry.CONTENT_URI, contentValues);
                    Log.d(LOG_TAG, "Added to the fav");
                } else {
                    //Updating is_favourite column in Most_Pop table by 0
                    db.execSQL("UPDATE " + MovieContract.MostPopMovieEntry.TABLE_NAME + " SET " +
                            MovieContract.MostPopMovieEntry.COLUMN_IS_FAVOURITE + " = '0' WHERE "
                            + MovieContract.MostPopMovieEntry._ID + " = "
                            + query_movie.getInt(query_movie.getColumnIndex(MovieContract.MostPopMovieEntry._ID)));

                    //Delete from favorite table
                    getContentResolver().delete(MovieContract.FavouriteMoviesEntry.CONTENT_URI,
                            MovieContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{String.valueOf(movieId)});
                    Log.d(LOG_TAG, "Deleted from Fav table");
                }
            } else {
                Log.d(LOG_TAG, "Cursor is null");
            }
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
                is_favourite = query_movie.getInt(query_movie.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_IS_FAVOURITE));

                if (is_favourite == 0) {

                    // Updating is_favourite column in Top_rated table by 1
                    db.execSQL("UPDATE " + MovieContract.TopRatedMovieEntry.TABLE_NAME + " SET " +
                            MovieContract.TopRatedMovieEntry.COLUMN_IS_FAVOURITE + " = '1' WHERE "
                            + MovieContract.TopRatedMovieEntry._ID + " = "
                            + query_movie.getInt(query_movie.getColumnIndex(MovieContract.TopRatedMovieEntry._ID)));

                    getContentResolver().insert(MovieContract.FavouriteMoviesEntry.CONTENT_URI, contentValues);
                    Log.d(LOG_TAG, "Added to the fav");
                } else {

                    //Updating is_favourite column in Top_rated table by 0
                    db.execSQL("UPDATE " + MovieContract.TopRatedMovieEntry.TABLE_NAME + " SET " +
                            MovieContract.TopRatedMovieEntry.COLUMN_IS_FAVOURITE + " = '0' WHERE "
                            + MovieContract.TopRatedMovieEntry._ID + " = "
                            + query_movie.getInt(query_movie.getColumnIndex(MovieContract.TopRatedMovieEntry._ID)));

                    //Delete from favorite table
                    getContentResolver().delete(MovieContract.FavouriteMoviesEntry.CONTENT_URI,
                            MovieContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{String.valueOf(movieId)});
                    Log.d(LOG_TAG, "Deleted from Fav table");
                }
            }
        }
        Button button = (Button) view.findViewById(R.id.addfav);
        if (is_favourite == 0) {
            button.setText("REMOVE FROM FAVORITE");
            button.setBackgroundColor(getResources().getColor(R.color.light_grey));
        } else {
            button.setText("ADD TO FAVORITE");
            button.setBackgroundColor(getResources().getColor(R.color.light_teal));
        }
        query_movie.close();
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
        } else if (id == R.id.home) {
            this.finish();
            return true;
        } else if (id == R.id.favPage) {
            Intent intent = new Intent(this, FavouriteActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
