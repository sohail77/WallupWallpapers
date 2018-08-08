package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class FeaturedCollectionModel {
    @SerializedName("id") private int id;
    @SerializedName("title") private String title;
    @SerializedName("user") private User user;
    @SerializedName("cover_photo") private CoverPhoto coverPhoto;
    @SerializedName("curated") private boolean isCurated;

    public boolean isCurated() {
        return isCurated;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getUser() {
        return user;
    }

    public CoverPhoto getCoverPhoto() {
        return coverPhoto;
    }
}
