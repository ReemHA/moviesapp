package com.example.user.movieproject.model;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.movieproject.R;
import com.squareup.picasso.Picasso;

/**
 * Created by USER on 8/22/2015.
 */

public class MovieGridCustomAdapter extends CursorAdapter {
    Context context;
    int resourceId;
    Cursor movies;
    public MovieGridCustomAdapter(Context context, int layoutResourceId, Cursor movies){
        super(context,movies, layoutResourceId);
        this.context=context;
        this.resourceId = layoutResourceId;
        this.movies = movies;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.movie_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            holder.image_id = (TextView) view.findViewById(R.id.movie_id);
            holder.movie_poster = (ImageView)view.findViewById(R.id.poster);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        String poster = movies.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
        if (poster == null){
            Picasso.with(context)
                    .load(R.drawable.unavailable)
                    .resizeDimen(R.dimen.width_poster, R.dimen.height_poster)
                    .centerInside()
                    .tag(context)
                    .into(holder.movie_poster);
        }else{
            Picasso.with(context)
                    .load(poster)
                    .resizeDimen(R.dimen.width_poster, R.dimen.height_poster)
                    .centerInside()
                    .tag(context)
                    .into(holder.movie_poster);
        }
        return;
    }

    static class ViewHolder{
        TextView image_id;
        ImageView movie_poster;
    }
}
