package com.example.mymoviesapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymoviesapp.R;
import com.example.mymoviesapp.database.MovieModelView;
import com.example.mymoviesapp.model.Movie;
import com.example.mymoviesapp.model.OnClickAdapter;
import com.example.mymoviesapp.model.Review;
import com.example.mymoviesapp.model.Videos;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context activity;
    private List<Movie> movieList;
    private List<Review> reviewList;
    private List<Videos> videosList;

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    public void setVideosList(List<Videos> videosList) {
        this.videosList = videosList;
        notifyDataSetChanged();
    }

    private OnClickAdapter clickAdapter;
    private MovieModelView movieModelView;
    private int value;

    public void setValue(int value) {
        this.value = value;
    }

    public RecycleAdapter(Context activity) {
        this.activity = activity;

    }


    public void setMovieModelView(MovieModelView movieModelView) {
        this.movieModelView = movieModelView;
    }

    public void setClickAdapter(OnClickAdapter clickAdapter) {
        this.clickAdapter = clickAdapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = null;
        switch (value) {
            case Movie.GET_MOVIE:
                v = LayoutInflater.from(activity).inflate(R.layout.row_movie, viewGroup, false);
                return new ViewHolder1(v);
            case Movie.GET_REVIEWS:
                v = LayoutInflater.from(activity).inflate(R.layout.row_reviews, viewGroup, false);
                return new ViewHolder2(v);
            case Movie.GET_TRAILERS:
                v = LayoutInflater.from(activity).inflate(R.layout.row_trailer, viewGroup, false);
                return new ViewHolder3(v);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        value = getItemViewType(i);
        switch (value) {
            case Movie.GET_MOVIE:
                ViewHolder1 holder1 = (RecycleAdapter.ViewHolder1) viewHolder;
                holder1.bindView(movieList.get(i), clickAdapter);
                break;
            case Movie.GET_REVIEWS:
                ViewHolder2 holder2 = (ViewHolder2) viewHolder;
                holder2.bindView(reviewList.get(i),i);
                break;
            case Movie.GET_TRAILERS:
                ViewHolder3 holder3 = (ViewHolder3) viewHolder;
                holder3.bindView(videosList.get(i),clickAdapter,i);
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (value){
            case Movie.GET_MOVIE:
                return movieList.size();

            case Movie.GET_REVIEWS:
                return reviewList.size();
            case Movie.GET_TRAILERS:
                return videosList.size();
        }
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        return value;
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        @BindView(R.id.id_poster_image)
        ImageView poster_image;
        @BindView(R.id.id_favorite_image)
        ImageView fav_image;
        @BindView(R.id.id_movie_title)
        TextView title;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(final Movie movie, final OnClickAdapter onClickAdapter) {
            String posterUrl = NetworkUtils.buildURL(movie.getMovie_posterPath().substring(1), 2);
            Picasso.get().load(posterUrl)
                    .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .into(poster_image);
            title.setText(movie.getMovie_title());
            poster_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAdapter.onClickAdapter(movie);
                }
            });
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAdapter.onClickAdapter(movie);
                }
            });
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        @BindView(R.id.id_row_review_id)
        TextView review_id;
        @BindView(R.id.id_row_review_author)
        TextView review_author;
        @BindView(R.id.id_row_review_content)
        TextView review_content;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void bindView(Review review,int position){
            StringBuilder builder = new StringBuilder();
            builder.append("Review: ");
            builder.append(position + 1);
            review_id.setText(builder);
            review_author.setText(review.getAuthor());
            review_content.setText(review.getContent());
        }
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder {
        @BindView(R.id.id_trailer_text)
        TextView trailer_id;
        @BindView(R.id.id_trailer_image)
        ImageView icon;
        @BindView(R.id.id_trailer_share)
        ImageView share;

        public ViewHolder3(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void bindView(final Videos video, final OnClickAdapter onClickAdapter, int position){
            StringBuilder builder = new StringBuilder();
            builder.append("Trailer: ");
            builder.append(String.valueOf(position + 1));
            Log.i("Test Video",builder.toString());
            trailer_id.setText(builder.toString());
            trailer_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAdapter.onClickAdapter(video, 0);
                }
            });
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAdapter.onClickAdapter(video, 0);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAdapter.onClickAdapter(video, 1);
                }
            });
        }
    }
}
