package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class Urls {
    @SerializedName("regular") private String image_regular;
    @SerializedName("small") private String image_small;
    @SerializedName("raw") private String image_raw;
    @SerializedName("full") private String full;



    public String getImage_regular() {
        return image_regular;
    }

    public String getImage_small() {
        return image_small;
    }

    public String getFull() {
        return full;
    }

    public String getImage_raw() {
        return image_raw;
    }
}
