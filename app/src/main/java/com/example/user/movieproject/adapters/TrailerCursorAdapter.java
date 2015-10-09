package com.example.user.movieproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
public class TrailerCursorAdapter extends CursorAdapter {
    Context context;
    Cursor cursor;
    public TrailerCursorAdapter(Context context, Cursor data, int flag){
        super(context, data, flag);
        this.context = context;
        this.cursor = data;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_list_item, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.name = (TextView) view.findViewById(R.id.trailer_name);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int nameColIndex = cursor.getColumnIndex(MovieContract.TrailersEntry.COLUMN_NAME);
        holder.name.setText(cursor.getString(nameColIndex));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeKey = cursor.getString(cursor.getColumnIndex(MovieContract.TrailersEntry.COLUMN_YOUTUBE_KEY));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeKey));
                context.startActivity(intent);
            }
        });
        return;
    }

    static class ViewHolder{
        TextView name;
    }
}
