package com.example.user.movieproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by USER on 8/22/2015.
 */

public class MovieGridCustomAdapter extends ArrayAdapter<Movie> {
    Context context;
    int resourceId;
    ArrayList<Movie> movies;
    public MovieGridCustomAdapter(Context context, int layoutResourceId, ArrayList<Movie> movies){
        super(context, layoutResourceId, movies);
        this.context=context;
        this.resourceId = layoutResourceId;
        this.movies = movies;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.image_id = (TextView) convertView.findViewById(R.id.movie_id);
            holder.movie_poster = (ImageView)convertView.findViewById(R.id.poster);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context)
                .load(movies.get(position).getImage())
                .resizeDimen(R.dimen.width_poster, R.dimen.height_poster)
                .centerInside()
                .tag(context)
                .into(holder.movie_poster);

        return convertView;
    }
    static class ViewHolder{
        TextView image_id;
        ImageView movie_poster;
    }
}
