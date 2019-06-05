package com.example.mymoviesapp.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mymoviesapp.model.Movie;

import java.util.List;

public class MovieRepository {
    private final Application context;
    private MovieDAO movieDAO;
    private LiveData<List<Movie>> movies;

    public MovieRepository(Application application) {
        MovieDatabase db = MovieDatabase.getDatabase(application);
        context = application;
        movieDAO = db.movieDAO();
        movies = movieDAO.getAllMovies();

    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void insertMovie(Movie movie) {
        new insertMovieAsync(movieDAO).execute(movie);
    }

    private class insertMovieAsync extends AsyncTask<Movie, Void, Void> {
        private final MovieDAO movieDao;

        public insertMovieAsync(MovieDAO movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("Test Async Task","Loading");
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.insert(movies[0]);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.i("Test progress",String.valueOf(values));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("TestFavClicked", "Stored");
        }
    }

    public void deleteMovie(Movie movie) {

        new deleteMovieAsync(movieDAO).execute(movie);
    }

    private class deleteMovieAsync extends AsyncTask<Movie, Void, Void> {
        private final MovieDAO movieDao;

        public deleteMovieAsync(MovieDAO movieDAO) {
            this.movieDao = movieDAO;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.delete(movies[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("TestFavClicked", "Deleted");
        }
    }

    public Movie findMovie(String id){
        return movieDAO.findMovie(id);
    }
   /* public boolean findMovie(String id) {
        if (movieDAO.findMovie(id) != null)
            return true;
        else
            return false;
    }*/
}
