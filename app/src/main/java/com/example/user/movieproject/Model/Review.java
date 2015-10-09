package com.example.user.movieproject.model;

/**
 * Created by USER on 10/5/2015.
 */
public class Review {
    private String author, content, review_id, movie_id, url;

    public Review(String author, String content, String url, String review_id,String movie_id) {
        this.author = author;
        this.movie_id = movie_id;
        this.content = content;
        this.review_id = review_id;
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public String getReview_id() {
        return review_id;
    }

    public String getUrl() {
        return url;
    }
}
