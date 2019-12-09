package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class PhotoModel {
    @SerializedName("id") public String id;
    @SerializedName("user") private User user;
    @SerializedName("urls") private Urls urls;
    @SerializedName("likes") private int likes;
    @SerializedName("links") private links links;

    public com.sohail.wallupwallpapers.Models.links getLinks() {
        return links;
    }

    public String getId() {
        return id;
    }

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
