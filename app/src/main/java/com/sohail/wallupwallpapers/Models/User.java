package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class User {
    private String name;
    private String location;
    @SerializedName("profile_image") private ProfileImage profileImage;

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
