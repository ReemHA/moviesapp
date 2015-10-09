package com.example.user.movieproject.model;

import java.util.ArrayList;

public class Movie {
    private String id;
    private String title;
    private double rating;
    private String release_date;
    private String plot;
    private String image;
    private String review;
    private ArrayList<String> trailer;

    public Movie(String id, String title, double rating,String release_date, String plot,String image, ArrayList<String> trailer){
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.release_date = release_date;
        this.plot = plot;
        this.image = image;
       // this.review = review;
        this.trailer = trailer;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setTrailer(ArrayList<String> trailer) {
        this.trailer = trailer;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPlot() {
        return plot;
    }

    public String getImage() {
        return image;
    }

    public String getReview() {
        return review;
    }

    public ArrayList<String> getTrailer() {
        return trailer;
    }
}
