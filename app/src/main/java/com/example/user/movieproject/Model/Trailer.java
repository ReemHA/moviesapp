package com.example.user.movieproject.model;

/**
 * Created by USER on 10/5/2015.
 */
public class Trailer {
    private String name, trailer_id, youtube_key, movie_id;

    public Trailer(String name, String trailer_id, String youtube_key, String movie_id) {
        this.movie_id = movie_id;
        this.name = name;
        this.trailer_id = trailer_id;
        this.youtube_key = youtube_key;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public void setTrailer_id(String trailer_id) {
        this.trailer_id = trailer_id;
    }

    public void setYoutube_key(String youtube_key) {
        this.youtube_key = youtube_key;
    }

    public String getName() {
        return name;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public String getTrailer_id() {
        return trailer_id;
    }

    public String getYoutube_key() {
        return youtube_key;
    }
}