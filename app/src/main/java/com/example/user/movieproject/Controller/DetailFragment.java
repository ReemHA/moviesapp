package com.example.user.movieproject.controller;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.example.user.movieproject.R;
import com.example.user.movieproject.adapters.MovieDescAdapter;
import com.example.user.movieproject.adapters.ReviewCursorAdapter;
import com.example.user.movieproject.adapters.TrailerCursorAdapter;
import com.example.user.movieproject.data.MovieContract;

/**
 * Created by USER on 9/23/2015.
 */
public class DetailFragment extends Fragment {
    public static String MOVIE_URI_WITH_ID = "movieUriWithId";
    String LOG_TAG = DetailFragment.class.getSimpleName();
    private String movieId;
    int isFavourite;
    Uri movieUriWithId;

    public DetailFragment() {
        super();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        String pref = Utility.getSortPreference(getActivity());
        MergeAdapter mergeAdapter = new MergeAdapter();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_details_fragment);
        Cursor cursor;
        String className;
        Intent intent = getActivity().getIntent();
        movieUriWithId = intent.getData();
        className = intent.getExtras().getString("className");
        if (intent == null) {
            return null;
        }


      /*  Button button = (Button) inflater.inflate(R.layout.header_view, container, false).findViewById(R.id.addfav);

        if (isFavourite == 1) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }*/
        movieId = Utility.getMovieIdFromUri(getActivity(), movieUriWithId);
        if (className.equals(MainActivity.LOG_TAG)) {
            if (pref.equals("0")) {
                cursor = getActivity().getContentResolver().query(
                        MovieContract.MostPopMovieEntry.CONTENT_URI,
                        null,
                        MovieContract.MostPopMovieEntry._ID + " = ?",
                        new String[]{String.valueOf(MovieContract.MostPopMovieEntry.getIdFromUri(movieUriWithId))},
                        null);
            } else {
                cursor = getActivity().getContentResolver().query(
                        MovieContract.TopRatedMovieEntry.CONTENT_URI,
                        null,
                        MovieContract.TopRatedMovieEntry._ID + " = ?",
                        new String[]{String.valueOf(MovieContract.TopRatedMovieEntry.getIdFromUri(movieUriWithId))},
                        null);
            }
        } else {
            cursor = getActivity().getContentResolver().query(
                    MovieContract.FavouriteMoviesEntry.CONTENT_URI,
                    null,
                    MovieContract.FavouriteMoviesEntry._ID + " = ?",
                    new String[]{String.valueOf(MovieContract.FavouriteMoviesEntry.getIdFromUri(movieUriWithId))},
                    null);
        }


        Cursor trCursor = getActivity().getContentResolver().query(
                MovieContract.TrailersEntry.CONTENT_URI,
                new String[]{MovieContract.TrailersEntry._ID, MovieContract.TrailersEntry.COLUMN_NAME, MovieContract.TrailersEntry.COLUMN_YOUTUBE_KEY},
                MovieContract.TrailersEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieId},
                null);

        Cursor reCursor = getActivity().getContentResolver().query(
                MovieContract.ReviewsEntry.CONTENT_URI,
                new String[]{MovieContract.ReviewsEntry._ID, MovieContract.ReviewsEntry.COLUMN_AUTHOR, MovieContract.ReviewsEntry.COLUMN_TEXT},
                MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieId},
                null);
        mergeAdapter.addAdapter(new MovieDescAdapter(getActivity(), cursor, 0));
        mergeAdapter.addAdapter(new TrailerCursorAdapter(getActivity(), trCursor, 0));
        mergeAdapter.addAdapter(new ReviewCursorAdapter(getActivity(), reCursor, 0));
        listView.setAdapter(mergeAdapter);

       /* cursor.close();
        trailers.close();
        reviews.close();*/
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}