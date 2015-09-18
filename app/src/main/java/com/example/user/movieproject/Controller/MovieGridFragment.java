package com.example.user.movieproject.controller;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.user.movieproject.model.Movie;
import com.example.user.movieproject.model.MovieContract;
import com.example.user.movieproject.model.MovieGridCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static MovieGridCustomAdapter adapter;
    private GridView grid;
    private static String storedPreferences;
    private static String url;
    private static final String API_KEY = "YOUR API KEY";
    private static final int MOVIE_LOADER = 0;
    private static int pref;
    public MovieGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            getSortPreference();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        grid = (GridView) rootView.findViewById(R.id.movies_grid);
        grid.setOnItemClickListener(gridItemClickListener);

        //NEW CODE NEW CODE
        adapter = new MovieGridCustomAdapter(getActivity(), R.layout.movie_item, null);
        grid.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void getSortPreference(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        storedPreferences = preferences.getString("sort_method", "0");
        MovieGridFragmentTask movieFragmentTask = new MovieGridFragmentTask(getActivity());
        movieFragmentTask.execute(storedPreferences);
    }

    @Override
    public void onStart() {
        super.onStart();
        getSortPreference();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (pref == 0){
            String columnsToExtract[] = new String[]{
                    MovieContract.MostPopMovieEntry.COLUMN_POSTER_PATH};

            String whereClause = "(" + MovieContract.MostPopMovieEntry.COLUMN_TITLE + "NOTNULL)";

            return new CursorLoader(getActivity(), MovieContract.MostPopMovieEntry.CONTENT_URI, columnsToExtract, whereClause, null ,null);

        }else{
            String columnsToExtract[] = new String[]{
                    MovieContract.TopRatedMovieEntry.COLUMN_POSTER_PATH};

            String whereClause = "(" + MovieContract.TopRatedMovieEntry.COLUMN_TITLE + "NOTNULL)";

            return new CursorLoader(getActivity(), MovieContract.TopRatedMovieEntry.CONTENT_URI, columnsToExtract, whereClause, null ,null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    final AdapterView.OnItemClickListener gridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            Movie movieGrid = (Movie) adapter.getItem(position);
            intent.putExtra("movie_id", movieGrid.getId());
            intent.putExtra("movie_img", movieGrid.getImage());
            intent.putExtra("movie_title", movieGrid.getTitle());
            intent.putExtra("movie_rating", movieGrid.getRating());
            intent.putExtra("movie_plot", movieGrid.getPlot());
            intent.putExtra("movie_date", movieGrid.getRelease_date());
            startActivity(intent);
        }
    };


}