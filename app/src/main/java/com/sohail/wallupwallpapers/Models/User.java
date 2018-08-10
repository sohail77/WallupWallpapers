package com.sohail.wallupwallpapers.Models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username") private String username;
    @SerializedName("total_photos") private int total_photos;
    @SerializedName("instagram_username") private String instaName;
    @SerializedName("bio") private String bio;

    private String name;
    private String location;
    @SerializedName("profile_image") private ProfileImage profileImage;

    public int getTotal_photos() {
        return total_photos;
    }

    public String getInstaName() {
        return instaName;
    }

    public String getBio() {
        return bio;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getUsername() {
        return username;
    }
}
