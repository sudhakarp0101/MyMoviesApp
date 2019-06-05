package com.example.mymoviesapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailers{
    @SerializedName("results")
    private List<Videos> videosList;

    public List<Videos> getVideosList() {
        return videosList;
    }

    public void setVideosList(List<Videos> videosList) {
        this.videosList = videosList;
    }


}
