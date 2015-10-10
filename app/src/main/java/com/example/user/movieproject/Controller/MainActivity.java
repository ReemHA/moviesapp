package com.example.user.movieproject.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.user.movieproject.R;
import com.example.user.movieproject.sync.MovieSyncAdapter;

public class MainActivity extends AppCompatActivity implements Callback {
    static boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    static final String LOG_TAG = MainActivity.class.getSimpleName();
    String mSortPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSortPref = Utility.getSortPreference(this);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState != null) {
                return;
            }
           /* getFragmentManager().beginTransaction().replace(R.id.movie_detail_container,
                    new DetailFragment(), DETAILFRAGMENT_TAG).commit();*/

            MovieGridFragment movieGridFragment = new MovieGridFragment();
            movieGridFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_movies_list, movieGridFragment)
                    .commit();
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0x0.0p0f);
        }
//        MovieGridFragment movieGridFragment =  ((MovieGridFragment)getSupportFragmentManager()
//                .findFragmentById(R.id.fragment_movies_list));
//        movieGridFragment.setUseTodayLayout
        MovieSyncAdapter.initializeSyncAdapter(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String sortPref = Utility.getSortPreference(this);
        if (!sortPref.equals(mSortPref)) {
            MovieGridFragment movieGridFragment = (MovieGridFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies_list);
            if (movieGridFragment != null) {
                movieGridFragment.onPreferenceChange();
            }
            mSortPref = sortPref;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        } else if (id == R.id.favPage) {
            Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_refresh) {
            MovieSyncAdapter.syncImmediately(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemClick(Uri contentUri, String className) {
        if (mTwoPane) {
            // DetailFragment detailFragment = new DetailFragment();
            Log.d(LOG_TAG, "in a mTwoPane");
            Bundle args = new Bundle();
            //args.putInt(DetailFragment.KEY_POSITION, movieIndex);
            //detailFragment.setArguments(args);
            //getFragmentManager().beginTransaction().replace(R.id.movie_detail_container, detailFragment, DETAILFRAGMENT_TAG).commit();
            args.putParcelable(DetailFragment.MOVIE_URI_WITH_ID, contentUri);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, detailFragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            intent.putExtra("className", LOG_TAG);
            startActivity(intent);
        }
    }
}

