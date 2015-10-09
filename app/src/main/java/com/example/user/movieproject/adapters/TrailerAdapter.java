package com.example.user.movieproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.movieproject.R;
import com.example.user.movieproject.model.Trailer;

import java.util.ArrayList;

/**
 * Created by USER on 10/5/2015.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {
    Context context;
    int resourceId;
    ArrayList<Trailer> trailers;

    public TrailerAdapter(Context context, int layoutResourceId, ArrayList<Trailer> trailers) {
        super(context, layoutResourceId, trailers);
        this.context = context;
        this.resourceId = layoutResourceId;
        this.trailers = trailers;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.trailer_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(trailers.get(position).getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeKey = trailers.get(position).getYoutube_key();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeKey));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView name;
    }
}
