package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class PhotoStats {
    @SerializedName("downloads") private downloadsModel downloads;
    @SerializedName("views") private ViewsModel views;
    @SerializedName("likes") private likesModel likes;

    public downloadsModel getDownloads() {
        return downloads;
    }

    public ViewsModel getViews() {
        return views;
    }

    public likesModel getLikes() {
        return likes;
    }
}
