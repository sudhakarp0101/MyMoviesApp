package com.example.mymoviesapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.mymoviesapp.model.Movie;

import java.util.List;

@Dao
public interface MovieDAO {

    @Insert
    void insert(Movie... movie);

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAllMovies();

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM movie WHERE movie_id = :id")
    public Movie findMovie(String id);
}
