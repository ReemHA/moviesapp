package com.example.user.movieproject.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.user.movieproject.R;

public class FavouriteActivity extends AppCompatActivity implements Callback {
    static boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    static final String LOG_TAG = FavouriteActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState != null) {
                return;
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0x0.0p0f);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favourite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemClick(Uri contentUri, String ClassName) {
        if (mTwoPane) {
            Log.d(LOG_TAG, "in a mTwoPane");
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.MOVIE_URI_WITH_ID, contentUri);
            args.putString("className", LOG_TAG);
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
