package com.example.user.movieproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    AsyncTask<Void, Void, ArrayList<Movie>> task;
    static String stream;
    CustomArrayAdapter adapter;
    GridView grid;
    static String storedPreferences;
    public MainActivityFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
//            startTask(storedPreferences);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences.OnSharedPreferenceChangeListener listener;
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        storedPreferences = preferences.getString("sort_method", "0");
        startTask(storedPreferences);
        grid = (GridView) rootView.findViewById(R.id.movies_grid);
        grid.setOnItemClickListener(gridItemClickListener);
        return rootView;
    }


    final AdapterView.OnItemClickListener gridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            Movie movieGrid = (Movie) adapter.getItem(position);
            intent.putExtra("movie_id", movieGrid.getId());
            intent.putExtra("movie_img", movieGrid.getImage());
            intent.putExtra("movie_title", movieGrid.getTitle());
            intent.putExtra("movie_rating", movieGrid.getRating());
            intent.putExtra("movie_plot", movieGrid.getPlot());
            intent.putExtra("movie_date", movieGrid.getRelease_date());
            startActivity(intent);
        }
    };

    public void startTask(final String stored_preferences) {
        task = new AsyncTask<Void, Void, ArrayList<Movie>>() {
            String url_add;
            @Override
            protected ArrayList<Movie> doInBackground(Void... params) {
                if (stored_preferences.equals("0")) {
                    url_add = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=0bed95c67895bbde6f8d00e7e464c50a";

                } else {
                    url_add = "http://private-53fc-themoviedb.apiary-mock.com/3/movie/top_rated";
                }
                try {
                    URL url = new URL(url_add);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    Log.d("location", "inDoInBG");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    stream = sb.toString();
                    urlConnection.disconnect();
                    try {
                        return getMovieInfo(stream);
                    } catch (JSONException E) {
                        E.printStackTrace();
                    }
                } catch (MalformedURLException E) {
                    E.printStackTrace();
                } catch (IOException E) {
                    E.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Movie> movies) {
                adapter = new CustomArrayAdapter(getActivity(), R.layout.movie_item, movies);
                grid.setAdapter(adapter);
                super.onPostExecute(movies);
            }
        };
        task.execute();
    }

    public ArrayList<Movie> getMovieInfo(String stream) throws JSONException {
        ArrayList<Movie> moviesArrayList = new ArrayList<>();
        Log.d("location", "inGetMovieInfo");
        JSONObject ObjectsOfTweets = new JSONObject(stream);
        JSONArray listMovies = ObjectsOfTweets.getJSONArray("results");
        for (int i = 0; i < listMovies.length(); i++) {
            JSONObject movie = listMovies.getJSONObject(i);
            int movieID = movie.getInt("id");
            String title = movie.getString("title");
            double vote_average = movie.getDouble("vote_average");
            String release_date = movie.getString("release_date");
            String plot = movie.getString("overview");
            String poster_path = "http://image.tmdb.org/t/p/w185/" + movie.getString("poster_path");
            Movie movieItem = new Movie(movieID, title, vote_average, release_date, plot, poster_path);
            moviesArrayList.add(movieItem);
        }
        return moviesArrayList;
    }

}