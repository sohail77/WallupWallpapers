package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class ResultsArray {
    @SerializedName("urls") private Urls urls;

    public Urls getUrls() {
        return urls;
    }
}
