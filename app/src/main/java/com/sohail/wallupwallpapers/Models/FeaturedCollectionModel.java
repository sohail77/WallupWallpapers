package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class FeaturedCollectionModel {
    @SerializedName("id") private int id;
    @SerializedName("total_photos") private int total_photos;
    @SerializedName("title") private String title;
    @SerializedName("user") private User user;
    @SerializedName("cover_photo") private CoverPhoto coverPhoto;
    @SerializedName("curated") private boolean isCurated;

    public boolean isCurated() {
        return isCurated;
    }

    public int getTotal_photos() {
        return total_photos;
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
