package com.example.mymoviesapp.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.mymoviesapp.model.Movie;

import java.util.List;

public class MovieModelView extends AndroidViewModel {
    private MovieRepository movieRepository;


    private LiveData<List<Movie>> movies;

    public MovieModelView(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        movies = movieRepository.getMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void insertMovie(Movie movie) {
        movieRepository.insertMovie(movie);
    }

    public void deleteMovie(Movie movie) {
        movieRepository.deleteMovie(movie);
    }

    public Movie findMovie(String id){
        return movieRepository.findMovie(id);
    }
}
