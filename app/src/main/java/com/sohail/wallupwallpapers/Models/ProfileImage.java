package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class ProfileImage {
    @SerializedName("small") private String image_small;
    @SerializedName("medium") private String image_medium;
    @SerializedName("large") private String image_large;

    public String getImage_small() {
        return image_small;
    }

    public String getImage_medium() {
        return image_medium;
    }

    public String getImage_large() {
        return image_large;
    }
}
