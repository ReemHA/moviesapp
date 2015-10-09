package com.example.user.movieproject.controller;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.user.movieproject.R;
import com.example.user.movieproject.adapters.MovieGridCustomAdapter;
import com.example.user.movieproject.data.MovieContract;

public class FavouriteActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>, Callback {
    static final int FAV_MOVIE_LOADER = 2;
    private static final String LOG_TAG = FavouriteActivity.class.getSimpleName();
    MovieGridCustomAdapter movieAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        GridView grid = (GridView) this.findViewById(R.id.fav_movies_grid);
        movieAdapter = new MovieGridCustomAdapter(getApplicationContext(), null, 0);
        grid.setAdapter(movieAdapter);
        grid.setOnItemClickListener(gridItemClickListener);
        getSupportLoaderManager().initLoader(FAV_MOVIE_LOADER, null, this);
    }

    final AdapterView.OnItemClickListener gridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            if (cursor != null) {
                ((Callback) getApplicationContext())
                        .OnItemClick(MovieContract.FavouriteMoviesEntry.buildMovieWithId(id));
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favourite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        android.support.v4.content.CursorLoader cursorLoader;
        cursorLoader = new android.support.v4.content.CursorLoader(
                getApplicationContext(), MovieContract.FavouriteMoviesEntry.CONTENT_URI,
                new String[]{MovieContract.FavouriteMoviesEntry._ID, MovieContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH},
                null,
                null,
                null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, data.getCount() + " rows loaded");
        movieAdapter.swapCursor(null);


    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);

    }

    @Override
    public void OnItemClick(Uri contentUri) {
        Intent intent = new Intent(this, DetailActivity.class)
                .setData(contentUri);
        startActivity(intent);
    }
}
