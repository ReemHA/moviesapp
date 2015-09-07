package com.example.user.movieproject.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.movieproject.Controller.SettingsActivity;
import com.example.user.movieproject.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int id_temp = extras.getInt("movie_id");
        String image_temp = extras.getString("movie_img");
        String title_temp = extras.getString("movie_title");
        String plot_temp = extras.getString("movie_plot");
        double rating_temp = extras.getDouble("movie_rating");
        String date_temp = extras.getString("movie_date");
        String year = date_temp.split("-")[0];

        TextView id = (TextView) findViewById(R.id.movie_id);
        LinearLayout title_layout = (LinearLayout) findViewById(R.id.movie_title_layout);
        TextView voting = (TextView) findViewById(R.id.rating);
        TextView date = (TextView) findViewById(R.id.date);
        TextView plot = (TextView) findViewById(R.id.plot);
        ImageView poster = (ImageView) findViewById(R.id.movie_poster);

        TextView title = (TextView) title_layout.findViewById(R.id.movie_title);
        title.setText(title_temp);
        voting.setText(Double.toString(rating_temp) + "/10");
        date.setText(year);
        plot.setText(plot_temp);
        plot.setMovementMethod(new ScrollingMovementMethod());

        Picasso.with(this.getApplicationContext())
                .load(image_temp)
                .resizeDimen(R.dimen.width_poster_detail, R.dimen.height_poster_detail)
                .centerInside()
                .into(poster);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if(id == R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
