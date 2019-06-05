package com.example.mymoviesapp.activities;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.mymoviesapp.BuildConfig;
import com.example.mymoviesapp.R;
import com.example.mymoviesapp.database.MovieModelView;
import com.example.mymoviesapp.model.Movie;
import com.example.mymoviesapp.model.OnClickAdapter;
import com.example.mymoviesapp.model.RetrofitApi;
import com.example.mymoviesapp.model.Review;
import com.example.mymoviesapp.model.Reviews;
import com.example.mymoviesapp.model.Trailers;
import com.example.mymoviesapp.model.Videos;
import com.example.mymoviesapp.utils.NetworkUtils;
import com.example.mymoviesapp.utils.RecycleAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.id_title)
    TextView title;
    @BindView(R.id.id_plot_movie)
    TextView summary;
    @BindView(R.id.id_rating_movie)
    TextView rating;
    @BindView(R.id.id_movie_date)
    TextView releaseDate;
    @BindView(R.id.id_image_poster)
    ImageView posterImage;
    @BindView(R.id.id_backdrop_image)
    ImageView backdropImage;
    @BindView(R.id.id_favorite_button)
    ToggleButton fav_button;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.id_trailers)
    Button showTrailers;
    @BindView(R.id.id_reviews_text_view)
    TextView reviewsText;
    @BindView(R.id.id_reviews_list_view)
    RecyclerView reviewListView;
    @BindView(R.id.id_detials_layout)
    CoordinatorLayout layout;
    private RecyclerView trailerListView;
    private RecycleAdapter recycleReviewAdapter, recycleTrailerAdapter;
    private MovieModelView movieModelView;
    private Movie movie;
    private RetrofitApi retrofitApi;
    private List<Videos> trailerList;
    private List<Review> reviewList;
    private Dialog dialog;
    private int pos = RecyclerView.NO_POSITION;
    private LinearLayoutManager layoutManager;
    private Bundle outState;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        layoutManager = new LinearLayoutManager(this);
        reviewListView.setLayoutManager(layoutManager);
        recycleReviewAdapter = new RecycleAdapter(this);
        recycleTrailerAdapter = new RecycleAdapter(this);
        movieModelView = ViewModelProviders.of(this).get(MovieModelView.class);
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        dialog = new Dialog(DetailsActivity.this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApi.GET_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitApi = retrofit.create(RetrofitApi.class);
        backdropImage = findViewById(R.id.id_backdrop_image);

        movie = getIntent().getParcelableExtra("movie");

        collapsingToolbar.setTitle(movie.getMovie_title());
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);
        title.setText(movie.getMovie_title());
        summary.setText(movie.getMovie_overview());
        String s[] = movie.getMovie_release_date().split("-");
        releaseDate.setText(s[0]);
        rating.setText(movie.getMovie_vote_average());
        String posterUrl = NetworkUtils.buildURL(movie.getMovie_posterPath().substring(1), 2);
        Picasso.get().load(posterUrl)
                .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(posterImage);
        String backdropUrl = NetworkUtils.buildURL(movie.getMovie_backdrop_path().substring(1), 3);
        Picasso.get().load(backdropUrl)
                .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(backdropImage);
        if (savedInstanceState == null) {
            getReviews();
            createDialog();
        } else {

            if (dialog != null) {
                dialog.show();
            }
            pos = savedInstanceState.getInt("position");
            Log.e("Test On Create", "" + pos);
            reviewList = savedInstanceState.getParcelableArrayList("reviews");
            addToView();
            if (pos == RecyclerView.NO_POSITION)
                pos = 0;
            reviewListView.smoothScrollToPosition(pos);

        }

        if (checkMovie())
            fav_button.setChecked(true);
        else
            fav_button.setChecked(false);

        fav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fav_button.isChecked()) {
                    if (checkMovie()) {
                        movieModelView.deleteMovie(movie);
                    }
                } else {
                    if (!checkMovie()) {
                        movieModelView.insertMovie(movie);
                    }
                }
            }
        });

        showTrailers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                getTrailers();
                recycleTrailerAdapter.setClickAdapter(new OnClickAdapter() {
                    @Override
                    public void onClickAdapter(Movie movie) {

                    }

                    @Override
                    public void onClickAdapter(Videos video, int i) {
                        String url = NetworkUtils.buildURL(video.getVideoURL(), Movie.GET_YOUTUBE_VIDEO);
                        switch (i) {
                            case 0:
                                Uri uri = Uri.parse(url);
                                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                                break;
                            case 1:
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("text/plain");
                                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                share.putExtra(Intent.EXTRA_SUBJECT, "Sharing Youtube Video :" + movie.getMovie_title());
                                share.putExtra(Intent.EXTRA_TEXT, url.toString());
                                startActivity(Intent.createChooser(share, "Share link!"));


                        }
                    }
                });
            }
        });
    }

    public void addToView() {

        recycleReviewAdapter.setReviewList(reviewList);
        recycleReviewAdapter.setValue(Movie.GET_REVIEWS);
        reviewListView.setAdapter(recycleReviewAdapter);
        recycleReviewAdapter.notifyDataSetChanged();

    }

    public void createDialog() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View content = inflater.inflate(R.layout.trailer_layout, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.7f);
        int dialogWindowHeight = (int) (displayHeight * 0.5f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.setContentView(content);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setTitle(movie.getMovie_title() + " Trailers: ");
        if (!dialog.isShowing()) {
            trailerListView = content.findViewById(R.id.id_trailer_list_view);
            trailerListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }

    }

    public boolean checkMovie() {
        Movie m = movieModelView.findMovie(String.valueOf(movie.getMovie_id()));
        if (m == null)
            return false;
        else
            return true;
    }

    public void getTrailers() {
        Call<Trailers> trailersCall = retrofitApi.getVideos(String.valueOf(movie.getMovie_id()), BuildConfig.MovieDbAPIKey);
        trailersCall.enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Call<Trailers> call, Response<Trailers> response) {
                Trailers trailers = response.body();
                trailerList = trailers.getVideosList();
                if (trailerList.size()==0) {
                    Snackbar.make(layout, "No Trailers to display", Snackbar.LENGTH_SHORT).show();
                    if(dialog.isShowing())
                        dialog.dismiss();
                }
                else {
                    recycleTrailerAdapter.setVideosList(trailerList);
                    recycleTrailerAdapter.setValue(Movie.GET_TRAILERS);
                    trailerListView.setHasFixedSize(true);
                    trailerListView.setAdapter(recycleTrailerAdapter);
                    recycleTrailerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Trailers> call, Throwable t) {

            }
        });

    }

    public void getReviews() {
        Call<Reviews> reviewsCall = retrofitApi.getReviews(String.valueOf(movie.getMovie_id()), BuildConfig.MovieDbAPIKey);
        reviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                Reviews reviews = response.body();
                reviewList = reviews.getReviews();
                if(reviewList.size()==0) {
                    Snackbar.make(layout, "No Reviews to display", Snackbar.LENGTH_SHORT).show();
                    reviewsText.setText(getResources().getString(R.string.no_reviews_text));
                }
                else {
                    reviewsText.setText(getResources().getString(R.string.reviews));
                    recycleReviewAdapter.setReviewList(reviewList);
                    recycleReviewAdapter.setValue(Movie.GET_REVIEWS);
                    reviewListView.setAdapter(recycleReviewAdapter);
                    recycleReviewAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        layoutManager = (LinearLayoutManager) reviewListView.getLayoutManager();
        assert layoutManager != null;
        pos = layoutManager.findFirstCompletelyVisibleItemPosition();
        outState.putInt("position", pos);
        Log.e("Test Review Pos", "" + pos);
        if (reviewList != null)
            outState.putParcelableArrayList("reviews", (ArrayList<? extends Parcelable>) reviewList);

    }

}
