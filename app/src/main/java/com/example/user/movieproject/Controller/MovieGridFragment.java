package com.example.user.movieproject.controller;

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

import com.example.user.movieproject.R;
import com.example.user.movieproject.model.Movie;
import com.example.user.movieproject.model.MovieGridCustomAdapter;

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
public class MovieGridFragment extends Fragment {
    private static String stream;
    public static MovieGridCustomAdapter adapter;
    public static GridView grid;
    private static String storedPreferences;
    private static String url;
    private static final String API_KEY = "0bed95c67895bbde6f8d00e7e464c50a";
    public MovieGridFragment() {
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
            MovieGridFragmentTask movieFragmentTask = new MovieGridFragmentTask();
            movieFragmentTask.execute(url);
            Log.d("value", storedPreferences);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        storedPreferences = preferences.getString("sort_method", "0");
        if(storedPreferences.equals("0")){
            url = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+API_KEY;
        }else{
            url= "http://private-53fc-themoviedb.apiary-mock.com/3/movie/top_rated";
        }
        MovieGridFragmentTask movieFragmentTask = new MovieGridFragmentTask();
        movieFragmentTask.execute(url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        grid = (GridView) rootView.findViewById(R.id.movies_grid);
        grid.setOnItemClickListener(gridItemClickListener);
        return rootView;
    }



    final AdapterView.OnItemClickListener gridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Callback listener = (Callback) getActivity();
            listener.OnItemClick(position);
            /*Intent intent = new Intent(view.getContext(), DetailActivity.class);
            Movie movieGrid = adapter.getItem(position);
            intent.putExtra("movie_id", movieGrid.getId());
            intent.putExtra("movie_img", movieGrid.getImage());
            intent.putExtra("movie_title", movieGrid.getTitle());
            intent.putExtra("movie_rating", movieGrid.getRating());
            intent.putExtra("movie_plot", movieGrid.getPlot());
            intent.putExtra("movie_date", movieGrid.getRelease_date());
            startActivity(intent);*/
        }
    };


    private class MovieGridFragmentTask extends AsyncTask<String, Void, ArrayList<Movie>>{
        public MovieGridFragmentTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                Log.d("location", url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
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
            super.onPostExecute(movies);
            adapter = new MovieGridCustomAdapter(getActivity(), R.layout.movie_item, movies);
            grid.setAdapter(adapter);

        }


    }

    private ArrayList<Movie> getMovieInfo(String stream) throws JSONException {
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
            for (Movie x : moviesArrayList){
                Log.d("movie", x.getTitle()+" "+x.getImage()+" "+x.getPlot());
            }
        }
        return moviesArrayList;
    }

}