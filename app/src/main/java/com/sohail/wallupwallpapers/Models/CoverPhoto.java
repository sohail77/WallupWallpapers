package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class CoverPhoto {
    @SerializedName("urls") private Urls urls;

    public Urls getUrls() {
        return urls;
    }
}
