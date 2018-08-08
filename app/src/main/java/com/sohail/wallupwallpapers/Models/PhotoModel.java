package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class PhotoModel {
    @SerializedName("user") private User user;
    @SerializedName("urls") private Urls urls;
    @SerializedName("likes") private int likes;

    public User getUser() {
        return user;
    }

    public Urls getUrls() {
        return urls;
    }

    public int getLikes() {
        return likes;
    }

}
