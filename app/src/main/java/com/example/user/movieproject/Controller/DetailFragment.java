package com.example.user.movieproject.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.movieproject.R;
import com.example.user.movieproject.model.MovieGridCustomAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by USER on 9/23/2015.
 */
public class DetailFragment extends Fragment {
    int mCurrentPosition = -1;
    static MovieGridCustomAdapter mMovieDetails;
    TextView mId;
    TextView mRating;
    TextView mDate;
    TextView mPlot;
    ImageView mPoster;
    TextView mTitle;
    final static String KEY_POSITION = "position";
    public DetailFragment() {
        super();
    }


    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null){
            setMovieDetails(args.getInt(KEY_POSITION));
        }else if (mCurrentPosition != -1){
            setMovieDetails(mCurrentPosition);
        }
    }

    public void setMovieDetails(int position){
        mTitle.setText(mMovieDetails.getItem(position).getTitle());
        mRating.setText(Double.toString(mMovieDetails.getItem(position).getRating()) + "/10");
        mDate.setText(mMovieDetails.getItem(position).getRelease_date().split("-")[0]);
        mPlot.setText(mMovieDetails.getItem(position).getPlot());
        mPlot.setMovementMethod(new ScrollingMovementMethod());
        Picasso.with(getActivity().getApplicationContext())
                .load(mMovieDetails.getItem(position).getImage())
                .resizeDimen(R.dimen.width_poster_detail, R.dimen.height_poster_detail)
                .centerInside()
                .into(mPoster);
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_POSITION, mCurrentPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovieDetails = MovieGridFragment.adapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_detail, container, false);
        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        if (savedInstanceState != null){
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
        }
        mId = (TextView) rootView.findViewById(R.id.movie_id);
        LinearLayout title_layout = (LinearLayout) rootView.findViewById(R.id.movie_title_layout);
        mRating = (TextView) rootView.findViewById(R.id.rating);
        mDate = (TextView) rootView.findViewById(R.id.date);
        mPlot = (TextView) rootView.findViewById(R.id.plot);
        mPoster = (ImageView) rootView.findViewById(R.id.movie_poster);
        mTitle = (TextView) title_layout.findViewById(R.id.movie_title);
        if(MainActivity.mTwoPane){
        //setMovieDetails(mCurrentPosition);
        }else{
            if(extras == null ){
                return null;
            }else{
                long id_temp = extras.getLong("movie_id");
                String image_temp = extras.getString("movie_img");
                String title_temp = extras.getString("movie_title");
                String plot_temp = extras.getString("movie_plot");
                double rating_temp = extras.getDouble("movie_rating");
                String date_temp = extras.getString("movie_date");
                String year = date_temp.split("-")[0];

                mTitle.setText(title_temp);
                mRating.setText(Double.toString(rating_temp) + "/10");
                mDate.setText(year);
                mPlot.setText(plot_temp);
                mPlot.setMovementMethod(new ScrollingMovementMethod());

                Picasso.with(getActivity().getApplicationContext())
                        .load(image_temp)
                        .resizeDimen(R.dimen.width_poster_detail, R.dimen.height_poster_detail)
                        .centerInside()
                        .into(mPoster);
                }
        }
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
