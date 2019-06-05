package com.example.mymoviesapp.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApi {
    String GET_URL = "https://api.themoviedb.org/3/";


    String test_URL = "https://api.themoviedb.org/3/movie/373571/";
    @GET("movie/popular")
    Call<MovieList> getPopular(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieList> getTopRated(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<Trailers> getVideos(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<Reviews> getReviews(@Path("id") String id, @Query("api_key") String apiKey);
}
