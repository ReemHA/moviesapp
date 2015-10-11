package com.example.user.movieproject.controller;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.user.movieproject.R;
import com.example.user.movieproject.adapters.MovieGridCustomAdapter;
import com.example.user.movieproject.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavouriteActivityFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{
    private static MovieGridCustomAdapter favMovieAdapter;

    public static final String LOG_TAG = FavouriteActivityFragment.class.getSimpleName();
    private static GridView favGrid;
    private static final int FAV_MOVIES_LOADER = 2;

    public FavouriteActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().restartLoader(FAV_MOVIES_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        favGrid = (GridView) rootView.findViewById(R.id.fav_movies_grid);
        favMovieAdapter = new MovieGridCustomAdapter(getActivity(), null, 0);
        favGrid.setAdapter(favMovieAdapter);
        favGrid.setOnItemClickListener(favGridItemClickListener);
        return rootView;
    }

    final AdapterView.OnItemClickListener favGridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            if (cursor != null) {
                ((Callback) getActivity())
                        .OnItemClick(MovieContract.FavouriteMoviesEntry.buildMovieWithId(id), LOG_TAG);
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_favourite, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(FAV_MOVIES_LOADER, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        android.support.v4.content.CursorLoader cursorLoader;
        cursorLoader = new android.support.v4.content.CursorLoader(
                getActivity(), MovieContract.FavouriteMoviesEntry.CONTENT_URI,
                new String[]{MovieContract.FavouriteMoviesEntry._ID, MovieContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH},
                null,
                null,
                null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, data.getCount() + " rows loaded");
        favMovieAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        favMovieAdapter.swapCursor(null);

    }


}