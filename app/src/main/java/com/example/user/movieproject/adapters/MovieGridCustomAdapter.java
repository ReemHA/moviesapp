package com.example.user.movieproject.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
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
 * Created by USER on 8/22/2015.
 */

public class MovieGridCustomAdapter extends CursorAdapter {
    String LOG_TAG = MovieGridCustomAdapter.class.getSimpleName();

    public MovieGridCustomAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        ViewHolder holder;
        holder = new ViewHolder();
        holder.image_id = (TextView) view.findViewById(R.id.movie_id);
        holder.movie_poster = (ImageView) view.findViewById(R.id.poster);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int posterColIndex;
        ViewHolder holder = (ViewHolder) view.getTag();
        String storedPreferences = Utility.getSortPreference(context);
        if (storedPreferences.equals("0")) {
            posterColIndex = cursor.getColumnIndex(MovieContract.MostPopMovieEntry.COLUMN_POSTER_PATH);
        } else {
            posterColIndex = cursor.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_POSTER_PATH);
        }
        Log.d(LOG_TAG, cursor.getString(posterColIndex));
        Picasso.with(context)
                .load(Uri.parse("http://image.tmdb.org/t/p/w185" + cursor.getString(posterColIndex)).buildUpon().build())
                .placeholder(R.drawable.images)
                .resizeDimen(R.dimen.width_poster, R.dimen.height_poster)
                .centerInside()
                .tag(context)
                .into(holder.movie_poster);
        return;
    }

    static class ViewHolder {
        TextView image_id;
        ImageView movie_poster;

    }
}
