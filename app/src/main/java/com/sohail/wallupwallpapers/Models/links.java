package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class links {
    @SerializedName("url") private String downloadEndpoint;

    public String getDownloadEndpoint() {
        return downloadEndpoint;
    }
}
