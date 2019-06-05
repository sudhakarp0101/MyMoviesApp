package com.example.mymoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Videos implements Parcelable {
    @SerializedName("key")
    private String videoURL;

    protected Videos(Parcel in) {
        videoURL = in.readString();
    }

    public static final Creator<Videos> CREATOR = new Creator<Videos>() {
        @Override
        public Videos createFromParcel(Parcel in) {
            return new Videos(in);
        }

        @Override
        public Videos[] newArray(int size) {
            return new Videos[size];
        }
    };

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoURL);
    }
}
