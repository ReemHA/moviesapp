//package com.example.user.movieproject.adapters;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.util.Log;
//
//import com.example.user.movieproject.R;
//import com.example.user.movieproject.controller.DetailFragment;
//import com.example.user.movieproject.model.Review;
//import com.example.user.movieproject.model.Trailer;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//
//public class MyMultipleAsyncTask{
//    String LOG_TAG = MyMultipleAsyncTask.class.getSimpleName();
//    Context context;
//
//    public MyMultipleAsyncTask(Context context) {
//        this.context = context;
//    }
//
//    public void runTasks(String url1, String url2){
//        TrailerFragmentAsyncTask trailerFragmentAsyncTask = new TrailerFragmentAsyncTask();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            trailerFragmentAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url1);
//        } else {
//            trailerFragmentAsyncTask.execute(url1);
//        }
//
//        ReviewAsyncTask reviewAsyncTask = new ReviewAsyncTask();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            reviewAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url2);
//        } else {
//            reviewAsyncTask.execute(url2);
//        }
//
//    }
//
//    private class TrailerFragmentAsyncTask extends AsyncTask<String, Void, ArrayList<Trailer>> {
//        String stream;
//
//        @Override
//        protected ArrayList<Trailer> doInBackground(String... params) {
//            try {
//                URL url = new URL(params[0]);
//                Log.d(LOG_TAG, url.toString());
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }
//                stream = sb.toString();
//                urlConnection.disconnect();
//                try {
//                    return getTrailer(stream);
//                } catch (JSONException E) {
//                    E.printStackTrace();
//                }
//            } catch (MalformedURLException E) {
//                E.printStackTrace();
//            } catch (IOException E) {
//                E.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Trailer> trailers) {
//            super.onPostExecute(trailers);
//            DetailFragment.trailerAdapter = new TrailerAdapter(context, R.layout.trailer_list_item, trailers);
//            DetailFragment.mergeAdapter.addAdapter(DetailFragment.trailerAdapter);
//            return;
//        }
//    }
//
//    private class ReviewAsyncTask extends AsyncTask<String, Void, ArrayList<Review>> {
//        String stream;
//        @Override
//        protected ArrayList<Review> doInBackground(String... params) {
//            try {
//                URL url = new URL(params[0]);
//                Log.d(LOG_TAG, url.toString());
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }
//                stream = sb.toString();
//                urlConnection.disconnect();
//                try {
//
//                    return getReview(stream);
//                } catch (JSONException E) {
//                    E.printStackTrace();
//                }
//            } catch (MalformedURLException E) {
//                E.printStackTrace();
//            } catch (IOException E) {
//                E.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Review> reviews) {
//            super.onPostExecute(reviews);
//            DetailFragment.reviewsAdapter = new ReviewsAdapter(context, R.layout.reviews_list_item, reviews);
//            DetailFragment.mergeAdapter.addAdapter(DetailFragment.reviewsAdapter);
//            DetailFragment.listView.setAdapter(DetailFragment.mergeAdapter);
//
//        }
//    }
//
//    private ArrayList<Trailer> getTrailer(String stream) throws JSONException {
//        JSONObject ObjectsOfVideos = new JSONObject(stream);
//        String movieId = ObjectsOfVideos.getString("id");
//        JSONArray listVideos = ObjectsOfVideos.getJSONArray("results");
//        ArrayList<Trailer> trailers = new ArrayList<>();
//        for (int i = 1; i < listVideos.length(); i++) {
//            JSONObject video = listVideos.getJSONObject(i);
//            Trailer trailer = new Trailer(video.getString("name"), video.getString("id"), video.getString("key"), movieId);
//            trailers.add(trailer);
//        }
//        return trailers;
//    }
//
//    private ArrayList<Review> getReview(String stream) throws JSONException {
//        JSONObject ObjectsOfReviews = new JSONObject(stream);
//        String movieId = ObjectsOfReviews.getString("id");
//        JSONArray listReviews = ObjectsOfReviews.getJSONArray("results");
//        ArrayList<Review> reviews = new ArrayList<>();
//        for (int i = 1; i < listReviews.length(); i++) {
//            JSONObject review = listReviews.getJSONObject(i);
//            Review rev = new Review(review.getString("author"), review.getString("content"), review.getString("url"), movieId, review.getString("id"));
//            reviews.add(rev);
//        }
//        return reviews;
//    }
//}