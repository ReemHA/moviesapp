package com.example.user.movieproject.controller;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
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
import com.example.user.movieproject.sync.MovieSyncAdapter;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieGridFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static MovieGridCustomAdapter movieAdapter;
    String pref;
    public static final int MOVIE_LOADER = 0;
    public static GridView grid;
    public static final String LOG_TAG = MovieGridFragment.class.getSimpleName();

    public MovieGridFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    void onPreferenceChange(){
        updateMovies();
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }
    private void updateMovies() {
        MovieSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        grid = (GridView) rootView.findViewById(R.id.movies_grid);
        movieAdapter = new MovieGridCustomAdapter(getActivity(), null, 0);
        grid.setAdapter(movieAdapter);
        grid.setOnItemClickListener(gridItemClickListener);
        return rootView;
    }

    final AdapterView.OnItemClickListener gridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            if (cursor != null){
               if(Utility.getSortPreference(getActivity()).equals("0")){
                   ((Callback) getActivity())
                           .OnItemClick(MovieContract.MostPopMovieEntry.buildMovieWithId(id), LOG_TAG);
               }else{
                   ((Callback) getActivity())
                           .OnItemClick(MovieContract.TopRatedMovieEntry.buildMovieWithId(id), LOG_TAG);
               }
            }
            /*Intent intent = new Intent(view.getContext(), DetailActivity.class);
            Movie movieGrid = adapter.getItem(position);
            intent.putExtra("movie_id", movieGrid.getId());
            intent.putExtra("movie_img", movieGrid.getImage());
            intent.putExtra("movie_title", movieGrid.getTitle());
            intent.putExtra("movie_rating", movieGrid.getRating());
            intent.putExtra("movie_plot", movieGrid.getPlot());
            intent.putExtra("movie_date", movieGrid.getRelease_date());
            startActivity(intent);*/
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        android.support.v4.content.CursorLoader cursorLoader;
        String pref = Utility.getSortPreference(getActivity());
        String a;
        if (pref.equals("0")) {
            cursorLoader = new android.support.v4.content.CursorLoader(
                    getActivity(),
                    MovieContract.MostPopMovieEntry.CONTENT_URI,
                    new String[]{MovieContract.MostPopMovieEntry._ID, MovieContract.MostPopMovieEntry.COLUMN_POSTER_PATH},
                    null,
                    null,
                    null);
        } else {
            cursorLoader = new android.support.v4.content.CursorLoader(
                    getActivity(),
                    MovieContract.TopRatedMovieEntry.CONTENT_URI,
                    new String[]{
                            MovieContract.TopRatedMovieEntry._ID,
                            MovieContract.TopRatedMovieEntry.COLUMN_POSTER_PATH},
                    null,
                    null,
                    null);
        }
        if (cursorLoader != null) {
            a = "cursorLoader initiated in onCreateLoader is not null";
        } else {
            a = "cursorLoader initiated in onCreateLoader is null";
        }
        Log.d(LOG_TAG, a);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, data.getCount() + " rows loaded");
        if (data != null) {
            Log.d(LOG_TAG, "Data received onLoadfinished is no null");
        }else{
            Log.d(LOG_TAG, "Data received onLoadfinished is null");
        }
        movieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "Loader is reset");
        movieAdapter.swapCursor(null);
    }


}