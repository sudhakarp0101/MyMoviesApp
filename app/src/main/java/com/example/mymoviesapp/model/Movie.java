package com.example.mymoviesapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movie")
public class Movie implements Parcelable {

    public static final int GET_MOVIE = 1;
    public static final int GET_MOVIE_POSTER = 2;
    public static final int GET_MOVIE_BACKDROP = 3;
    public static final int GET_TRAILERS = 4;
    public static final int GET_REVIEWS = 5;
    public static final int GET_YOUTUBE_VIDEO = 6;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @SerializedName("id")
    private int movie_id;
    @ColumnInfo(name = "rating")
    @SerializedName("vote_average")
    private String movie_vote_average;
    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String movie_title;
    @ColumnInfo(name = "posterPath")
    @SerializedName("poster_path")
    private String movie_posterPath;
    @ColumnInfo(name = "backdropPath")
    @SerializedName("backdrop_path")
    private String movie_backdrop_path;
    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    private String movie_overview;
    @ColumnInfo(name = "date")
    @SerializedName("release_date")
    private String movie_release_date;


    public Movie() {
    }

    protected Movie(Parcel in) {
        movie_id = in.readInt();
        movie_vote_average = in.readString();
        movie_title = in.readString();
        movie_posterPath = in.readString();
        movie_backdrop_path = in.readString();
        movie_overview = in.readString();
        movie_release_date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_vote_average() {
        return movie_vote_average;
    }

    public void setMovie_vote_average(String movie_vote_average) {
        this.movie_vote_average = movie_vote_average;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public String getMovie_posterPath() {
        return movie_posterPath;
    }

    public void setMovie_posterPath(String movie_posterPath) {
        this.movie_posterPath = movie_posterPath;
    }

    public String getMovie_backdrop_path() {
        return movie_backdrop_path;
    }

    public void setMovie_backdrop_path(String movie_backdrop_path) {
        this.movie_backdrop_path = movie_backdrop_path;
    }

    public String getMovie_overview() {
        return movie_overview;
    }

    public void setMovie_overview(String movie_overview) {
        this.movie_overview = movie_overview;
    }

    public String getMovie_release_date() {
        return movie_release_date;
    }

    public void setMovie_release_date(String movie_release_date) {
        this.movie_release_date = movie_release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movie_id);
        dest.writeString(movie_vote_average);
        dest.writeString(movie_title);
        dest.writeString(movie_posterPath);
        dest.writeString(movie_backdrop_path);
        dest.writeString(movie_overview);
        dest.writeString(movie_release_date);
    }


}
