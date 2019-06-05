package com.example.mymoviesapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.example.mymoviesapp.model.Movie;

public final class NetworkUtils {
    private static String IMAGE_URL = "http://image.tmdb.org/t/p/w500";
    private static String BACKDROP_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_URL = "https://www.youtube.com/watch";
    private static String KEY_WATCH = "v";


    public static String buildURL(String query, int value) {
        Uri buildUri = null;
        if (value == Movie.GET_MOVIE_POSTER) {
            buildUri = Uri.parse(IMAGE_URL).buildUpon()
                    .appendPath(query).build();
        } else if (value == Movie.GET_MOVIE_BACKDROP) {

            buildUri = Uri.parse(BACKDROP_URL).buildUpon()
                    .appendPath(query).build();
        }
        else {
            buildUri = Uri.parse(YOUTUBE_URL).buildUpon()
                    .appendQueryParameter(KEY_WATCH, query)
                    .build();
        }

        return buildUri.toString();
    }


    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = null;
        if (connectivityManager != null) {
            info = connectivityManager.getActiveNetworkInfo();
        }
        if (info == null) {
            return false;
        }
        return true;
    }
}
