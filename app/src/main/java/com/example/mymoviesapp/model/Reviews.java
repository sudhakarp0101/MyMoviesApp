package com.example.mymoviesapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reviews{
    @SerializedName("results")
    private List<Review> reviews;

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
