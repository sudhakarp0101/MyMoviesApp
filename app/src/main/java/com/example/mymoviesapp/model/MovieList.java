package com.example.mymoviesapp.model;

import java.util.List;

public class MovieList {
    public List<Movie> getResults() {
        return results;
    }


    public MovieList(List<Movie> results) {
        this.results = results;
    }

    private List<Movie> results;
}
