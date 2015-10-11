package com.example.user.movieproject.controller;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    Uri movieUriWithId;
    public static Uri mUri;
    public DetailFragment() {
        super();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        String pref = Utility.getSortPreference(getActivity());
        MergeAdapter mergeAdapter = new MergeAdapter();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_details_fragment);
        Cursor cursor;
        String className = null;

        Cursor trCursor;
        Cursor reCursor;
        Bundle args = getArguments();
        if (args != null){
            mUri = args.getParcelable(DetailFragment.MOVIE_URI_WITH_ID);
            className = args.getString("className");
        }

        Log.d(LOG_TAG, (mUri) + "");
        movieId = Utility.getMovieIdFromUri(getActivity(), mUri);
        if (className.equals(MainActivity.LOG_TAG)) {
            if (pref.equals("0")) {
                cursor = getActivity().getContentResolver().query(
                        mUri,
                        null,
                        MovieContract.MostPopMovieEntry._ID + " = ?",
                        new String[]{String.valueOf(MovieContract.MostPopMovieEntry.getIdFromUri(mUri))},
                        null);
            } else {
                cursor = getActivity().getContentResolver().query(
                        mUri,
                        null,
                        MovieContract.TopRatedMovieEntry._ID + " = ?",
                        new String[]{String.valueOf(MovieContract.TopRatedMovieEntry.getIdFromUri(mUri))},
                        null);
            }

            trCursor = getActivity().getContentResolver().query(
                    MovieContract.TrailersEntry.CONTENT_URI,
                    new String[]{MovieContract.TrailersEntry._ID, MovieContract.TrailersEntry.COLUMN_NAME, MovieContract.TrailersEntry.COLUMN_YOUTUBE_KEY},
                    MovieContract.TrailersEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{movieId},
                    null);

            reCursor = getActivity().getContentResolver().query(
                    MovieContract.ReviewsEntry.CONTENT_URI,
                    new String[]{MovieContract.ReviewsEntry._ID, MovieContract.ReviewsEntry.COLUMN_AUTHOR, MovieContract.ReviewsEntry.COLUMN_TEXT},
                    MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{movieId},
                    null);
        } else {
            cursor = getActivity().getContentResolver().query(
                    mUri,
                    null,
                    MovieContract.FavouriteMoviesEntry._ID + " = ?",
                    new String[]{String.valueOf(MovieContract.FavouriteMoviesEntry.getIdFromUri(mUri))},
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                trCursor = getActivity().getContentResolver().query(
                        MovieContract.TrailersEntry.CONTENT_URI,
                        new String[]{MovieContract.TrailersEntry._ID, MovieContract.TrailersEntry.COLUMN_NAME, MovieContract.TrailersEntry.COLUMN_YOUTUBE_KEY},
                        MovieContract.TrailersEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(MovieContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID)))},
                        null);

                reCursor = getActivity().getContentResolver().query(
                        MovieContract.ReviewsEntry.CONTENT_URI,
                        new String[]{MovieContract.ReviewsEntry._ID, MovieContract.ReviewsEntry.COLUMN_AUTHOR, MovieContract.ReviewsEntry.COLUMN_TEXT},
                        MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(MovieContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID)))},
                        null);
            } else {
                trCursor = null;
                reCursor = null;
            }
        }

        mergeAdapter.addAdapter(new MovieDescAdapter(getActivity(), cursor, 0));
        mergeAdapter.addAdapter(new TrailerCursorAdapter(getActivity(), trCursor, 0));
        mergeAdapter.addAdapter(new ReviewCursorAdapter(getActivity(), reCursor, 0));
        listView.setAdapter(mergeAdapter);
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