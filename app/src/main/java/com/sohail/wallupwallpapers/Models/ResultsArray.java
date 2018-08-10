package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class ResultsArray {
    @SerializedName("urls") private Urls urls;
    @SerializedName("user") private User user;
    @SerializedName("id") private String id;

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Urls getUrls() {
        return urls;
    }
}
