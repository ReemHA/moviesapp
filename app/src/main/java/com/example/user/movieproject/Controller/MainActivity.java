package com.example.user.movieproject.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.user.movieproject.R;
import com.example.user.movieproject.model.Movie;

public class MainActivity extends AppCompatActivity implements Callback{
    static boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.movie_detail_container) != null){
            mTwoPane = true;
            if (savedInstanceState != null){
                return;
            }
            /*getFragmentManager().beginTransaction().replace(R.id.movie_detail_container,
                    new DetailFragment(), DETAILFRAGMENT_TAG).commit();*/

            MovieGridFragment movieGridFragment = new MovieGridFragment();
            movieGridFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_movies_list, movieGridFragment)
                    .commit();
        }else{
            mTwoPane = false;
            getSupportActionBar().setElevation(0x0.0p0f);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MovieGridFragment movieGridFragment = (MovieGridFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movies_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, com.example.user.movieproject.controller.SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemClick(int movieIndex) {
        if (mTwoPane){
            DetailFragment detailFragment = new DetailFragment();
            Log.d("WHERE", "mTwoPane");
            Bundle args = new Bundle();
            args.putInt(DetailFragment.KEY_POSITION, movieIndex);
            detailFragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.movie_detail_container, detailFragment, DETAILFRAGMENT_TAG).commit();
        }else {
            Log.d("WHERE", "No mTwoPane");
            Intent intent = new Intent(this, DetailActivity.class);
            Movie movieGrid = MovieGridFragment.adapter.getItem(movieIndex);
            intent.putExtra("movie_id", movieGrid.getId());
            intent.putExtra("movie_img", movieGrid.getImage());
            intent.putExtra("movie_title", movieGrid.getTitle());
            intent.putExtra("movie_rating", movieGrid.getRating());
            intent.putExtra("movie_plot", movieGrid.getPlot());
            intent.putExtra("movie_date", movieGrid.getRelease_date());
            startActivity(intent);
        }
    }
}
