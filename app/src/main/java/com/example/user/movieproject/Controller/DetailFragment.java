package com.example.user.movieproject.controller;

import android.app.Fragment;
import android.content.ContentValues;
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
import com.example.user.movieproject.adapters.ReviewsAdapter;
import com.example.user.movieproject.adapters.TrailerAdapter;
import com.example.user.movieproject.adapters.TrailerCursorAdapter;
import com.example.user.movieproject.data.MovieContract;

/**
 * Created by USER on 9/23/2015.
 */
public class DetailFragment extends Fragment {
    public static String MOVIE_URI_WITH_ID = "movieUriWithId";
    public static String MOVIE_ROW_ID = "MOVIE_ROW_ID";
    public static TrailerAdapter trailerAdapter;
    public static ReviewsAdapter reviewsAdapter;
    public static MovieDescAdapter movieDescAdapter;
    private String API_KEY = "0bed95c67895bbde6f8d00e7e464c50a";
    String LOG_TAG = DetailFragment.class.getSimpleName();
    private static MergeAdapter mergeAdapter;
    private static ListView listView;
    String movieId;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        String pref = Utility.getSortPreference(getActivity());
        Cursor cursor;
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_view_details_fragment);
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }
        int isFavourite;
        mergeAdapter = new MergeAdapter();
        Uri movieUriWithId = intent.getData();
        if (pref.equals("0")) {
            movieId = Utility.getMovieIdFromUri(getActivity(), movieUriWithId);
            cursor = getActivity().getContentResolver().query(
                    MovieContract.MostPopMovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MostPopMovieEntry._ID + " = ?",
                    new String[]{String.valueOf(MovieContract.MostPopMovieEntry.getIdFromUri(movieUriWithId))},
                    null);
            //isFavourite = cursor.getInt(cursor.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_IS_FAVOURITE));
        } else {
            movieId = Utility.getMovieIdFromUri(getActivity(), movieUriWithId);
            cursor = getActivity().getContentResolver().query(
                    MovieContract.TopRatedMovieEntry.CONTENT_URI,
                    null,
                    MovieContract.TopRatedMovieEntry._ID + " = ?",
                    new String[]{String.valueOf(MovieContract.TopRatedMovieEntry.getIdFromUri(movieUriWithId))},
                    null);
           // isFavourite = cursor.getInt(cursor.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_IS_FAVOURITE));
        }
        Cursor trailers = getActivity().getContentResolver().query(
                MovieContract.TrailersEntry.CONTENT_URI,
                new String[]{MovieContract.TrailersEntry._ID, MovieContract.TrailersEntry.COLUMN_NAME, MovieContract.TrailersEntry.COLUMN_YOUTUBE_KEY},
                MovieContract.TrailersEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieId},
                null
        );
        Cursor reviews = getActivity().getContentResolver().query(
                MovieContract.ReviewsEntry.CONTENT_URI,
                new String[]{MovieContract.ReviewsEntry._ID, MovieContract.ReviewsEntry.COLUMN_AUTHOR, MovieContract.ReviewsEntry.COLUMN_TEXT},
                MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieId},
                null
        );

        /*Button button = (Button) inflater.inflate(R.layout.header_view, container, false).findViewById(R.id.addfav);
        if (isFavourite == 1) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }*/

        movieDescAdapter = new MovieDescAdapter(this.getActivity(), cursor, 0);
        mergeAdapter.addAdapter(movieDescAdapter);
        mergeAdapter.addAdapter(new TrailerCursorAdapter(this.getActivity(), trailers, 0));
        mergeAdapter.addAdapter(new ReviewCursorAdapter(this.getActivity(), reviews, 0));
        listView.setAdapter(mergeAdapter);

        return rootView;
    }

    public void addToFav(View view) {
        ContentValues contentValues = new ContentValues();
        Cursor query_movie;
        int update_movie;
        if (Utility.getSortPreference(getActivity()).equals("0")) {
            query_movie = getActivity().getContentResolver().query(
                    MovieContract.MostPopMovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MostPopMovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{movieId},
                    null);
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

            ContentValues cv = new ContentValues();
            cv.put(MovieContract.MostPopMovieEntry.COLUMN_IS_FAVOURITE, 1);
            update_movie = getActivity().getContentResolver().update(
                    MovieContract.MostPopMovieEntry.CONTENT_URI,
                    cv,
                    MovieContract.MostPopMovieEntry.COLUMN_MOVIE_ID +" = ?",
                    new String[]{movieId});
    }

    else
    {
        query_movie = getActivity().getContentResolver().query(
                MovieContract.TopRatedMovieEntry.CONTENT_URI,
                null,
                MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieId},
                null);
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

        //update
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.TopRatedMovieEntry.COLUMN_IS_FAVOURITE, 1);
        getActivity().getContentResolver().update(
                MovieContract.TopRatedMovieEntry.CONTENT_URI,
                cv,
                MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_ID +" = ?",
                new String[]{movieId});
    }

    Uri insertedMovieUri =
            getActivity().getContentResolver().insert(MovieContract.FavouriteMoviesEntry.CONTENT_URI, contentValues);
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
