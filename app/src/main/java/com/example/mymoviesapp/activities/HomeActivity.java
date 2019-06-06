package com.example.mymoviesapp.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mymoviesapp.BuildConfig;
import com.example.mymoviesapp.R;
import com.example.mymoviesapp.database.MovieModelView;
import com.example.mymoviesapp.model.Movie;
import com.example.mymoviesapp.model.MovieList;
import com.example.mymoviesapp.model.OnClickAdapter;
import com.example.mymoviesapp.model.RetrofitApi;
import com.example.mymoviesapp.model.Videos;
import com.example.mymoviesapp.utils.RecycleAdapter;
import com.example.mymoviesapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.id_movie_list_view)
    RecyclerView movie_list_view;

    @BindView(R.id.id_home_layout)
    ConstraintLayout layout;

    private RecycleAdapter moviesAdapter;
    private List<Movie> movieList;
    private ProgressDialog progressDialog;
    private String type = "Popular";
    private int pos = RecyclerView.NO_POSITION;
    private GridLayoutManager layoutManager;
    private Snackbar snackbar;
    private MovieModelView movieModelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        movieModelView = ViewModelProviders.of(this).get(MovieModelView.class);
        if (getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            layoutManager = new GridLayoutManager(this, 2);
        else
            layoutManager = new GridLayoutManager(this, 3);
        movie_list_view.setLayoutManager(layoutManager);
        moviesAdapter = new RecycleAdapter(this);
        snackbar = Snackbar.make(layout, "Internet is not Available, Please check connection", Snackbar.LENGTH_SHORT);
        moviesAdapter.setClickAdapter(new OnClickAdapter() {
            @Override
            public void onClickAdapter(Movie movie) {
                Intent intent = new Intent(HomeActivity.this, DetailsActivity.class);
                intent.putExtra("movie", movie);
                Log.i("Test", "" + movie.getMovie_id());
                startActivity(intent);
            }

            @Override
            public void onClickAdapter(Videos video, int i) {

            }
        });
        movieModelView = ViewModelProviders.of(this).get(MovieModelView.class);
        if (savedInstanceState == null) {
            if (NetworkUtils.checkNetwork(this))
                getData(type);
            else
                snackbar.show();
        } else {
            pos = savedInstanceState.getInt("position");
            movieList = savedInstanceState.getParcelableArrayList("movies");
            addToView();
            if (pos == RecyclerView.NO_POSITION)
                pos = 0;
            movie_list_view.smoothScrollToPosition(pos);
        }
    }

    public void addToView() {

        moviesAdapter.setMovieList(movieList);
        moviesAdapter.setMovieModelView(movieModelView);
        moviesAdapter.setValue(Movie.GET_MOVIE);
        moviesAdapter.notifyDataSetChanged();
        movie_list_view.setAdapter(moviesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.id_popular:
                type = "Popular";
                if (NetworkUtils.checkNetwork(this))
                    getData(type);
                else
                    snackbar.show();
                break;
            case R.id.id_top_rated:
                type = "Top-Rated";
                if (NetworkUtils.checkNetwork(this))
                    getData(type);
                else
                    snackbar.show();
                break;
            case R.id.id_favorites:
                type = "Favorites";
                getDataFromRoom();
                break;
        }
        return true;
    }

    private void getDataFromRoom() {
        movieModelView.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                movieList = movies;
                if (movieList.size() != 0)
                    addToView();
                else {
                    addToView();
                    Snackbar.make(layout, "Favorite List is empty, Try adding some...!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getData(final String type) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApi.GET_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<MovieList> callMovie = retrofitApi.getPopular(BuildConfig.MovieDbAPIKey);
        progressDialog.setMessage("Downloading from Popular Movies Internet");
        if (type.equals("Top-Rated")) {
            callMovie = retrofitApi.getTopRated(BuildConfig.MovieDbAPIKey);
            progressDialog.setMessage("Downloading Top-Rated Movies from Internet");
        }
        callMovie.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                MovieList list = response.body();
                movieList = list.getResults();
                addToView();

            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.e("Error", t.getMessage());
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        pos = layoutManager.findFirstCompletelyVisibleItemPosition();
        outState.putInt("position", pos);
        outState.putParcelableArrayList("movies", (ArrayList<? extends Parcelable>) movieList);

    }
}
