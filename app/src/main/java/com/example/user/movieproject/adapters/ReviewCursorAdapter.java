package com.example.user.movieproject.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.user.movieproject.R;
import com.example.user.movieproject.data.MovieContract;

/**
 * Created by USER on 10/9/2015.
 */
public class ReviewCursorAdapter extends CursorAdapter {
    Context context;
    Cursor cursor;
    public ReviewCursorAdapter(Context context, Cursor data, int flag){
        super(context, data, flag);
        this.context = context;
        this.cursor = data;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.reviews_list_item, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.author_name = (TextView) view.findViewById(R.id.author_name);
        holder.content = (TextView) view.findViewById(R.id.review_content);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int AuthorNameColIndex = cursor.getColumnIndex(MovieContract.ReviewsEntry.COLUMN_AUTHOR);
        holder.author_name.setText(cursor.getString(AuthorNameColIndex));
        holder.content.setText(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewsEntry.COLUMN_TEXT)));
        return;
    }

    static class ViewHolder{
        TextView author_name;
        TextView content;
    }
}
