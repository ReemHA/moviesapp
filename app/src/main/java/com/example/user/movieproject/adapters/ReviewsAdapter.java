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
import com.example.user.movieproject.model.Review;

import java.util.ArrayList;

/**
 * Created by USER on 10/5/2015.
 */
public class ReviewsAdapter extends ArrayAdapter<Review> {
    Context context;
    int resourceId;
    ArrayList<Review> reviews;
    public ReviewsAdapter(Context context, int layoutResourceId, ArrayList<Review> reviews){
        super(context, layoutResourceId, reviews);
        this.context = context;
        this.resourceId = layoutResourceId;
        this.reviews = reviews;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.author_name = (TextView) convertView.findViewById(R.id.author_name);
            holder.content = (TextView) convertView.findViewById(R.id.review_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.author_name.setText(reviews.get(position).getAuthor());
        holder.content.setText(reviews.get(position).getContent());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewLink = reviews.get(position).getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewLink));
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder{
        TextView content;
        TextView author_name;
    }
}
