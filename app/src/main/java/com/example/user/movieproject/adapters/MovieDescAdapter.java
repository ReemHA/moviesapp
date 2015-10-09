package com.example.user.movieproject.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.movieproject.R;
import com.example.user.movieproject.controller.Utility;
import com.example.user.movieproject.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by USER on 10/8/2015.
 */
public class MovieDescAdapter extends CursorAdapter {

    public MovieDescAdapter(Context context, Cursor movies_header_details, int flag) {
        super(context, movies_header_details, flag);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_view, parent, false);
        ViewHolder holder;
        holder = new ViewHolder();
        holder.movie_id = (TextView) view.findViewById(R.id.movie_id);
        holder.movie_title = (TextView) view.findViewById(R.id.movie_title);
        holder.movie_poster = (ImageView) view.findViewById(R.id.movie_poster);
        holder.date = (TextView) view.findViewById(R.id.date);
        holder.rating = (TextView) view.findViewById(R.id.rating);
        holder.plot = (TextView) view.findViewById(R.id.plot);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int movie_id;
        String title;
        String poster;
        double rating;
        String releaseDate;
        String overview;
        int IS_FAVORITE;
        if (Utility.getSortPreference(context).equals("0")) {
            movie_id = cursor.getInt(cursor.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_MOVIE_ID));
            title = cursor.getString(cursor.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_TITLE));
            poster = cursor.getString(cursor.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_POSTER_PATH));
            rating = cursor.getDouble(cursor.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_VOTE_AVG));
            releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_RELEASE_DATE));
            overview = cursor.getString(cursor.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_PLOT));
            //IS_FAVORITE = cursor.getInt(cursor.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_IS_FAVOURITE));


        } else {
            movie_id = cursor.getInt(cursor.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_ID));
            title = cursor.getString(cursor.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_TITLE));
            poster = cursor.getString(cursor.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_POSTER_PATH));
            rating = cursor.getDouble(cursor.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_VOTE_AVG));
            releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_RELEASE_DATE));
            overview = cursor.getString(cursor.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_PLOT));
            //IS_FAVORITE = cursor.getInt(cursor.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_IS_FAVOURITE));
        }
        Picasso.with(context)
                .load(Uri.parse("http://image.tmdb.org/t/p/w185/" + poster).buildUpon().build())
                .resizeDimen(R.dimen.width_poster, R.dimen.height_poster)
                .centerInside()
                .tag(context)
                .into(holder.movie_poster);
        holder.movie_id.setText(String.valueOf(movie_id));
        holder.movie_title.setText(title);
        holder.rating.setText(String.valueOf(rating));
        holder.date.setText(releaseDate);
        holder.plot.setText(overview);

//        if (IS_FAVORITE == 0){
//
//        }
        return;
    }

    static class ViewHolder {
        TextView movie_id;
        TextView movie_title;
        ImageView movie_poster;
        TextView date;
        TextView rating;
        TextView plot;
    }

}
