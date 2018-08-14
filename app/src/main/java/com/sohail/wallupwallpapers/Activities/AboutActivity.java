package com.sohail.wallupwallpapers.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.sohail.wallupwallpapers.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.aboutimageicon)
                .addItem(versionElement)
                .addGroup("Connect with me")
                .setDescription("WallUp Wallpapers \n Developed By \n Mohammed Sohail Ahmed")
                .addEmail("sohail778899@gmail.com","Email me")
                .addFacebook("mohd.sohail.50951","Contact me on Facebook")
                .addTwitter("sohail7788","Contact me on Twitter")
                .addPlayStore("com.sohail.wallupwallpapers")
                .addInstagram("sohail77","Contact me on Instagram")
                .create();

        setContentView(aboutPage);
    }
}
