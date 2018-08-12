package com.sohail.wallupwallpapers.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
//                .setImage(R.drawable.dummy_image)
                .addItem(versionElement)
                .addGroup("Connect with me")
                .addEmail("sohail778899@gmail.com")
                .addWebsite("http://medyo.github.io/")
                .addFacebook("the.medy")
                .addTwitter("medyo80")
                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                .addPlayStore("com.sohail.wallupwallpapers")
                .addGitHub("sohail77")
                .addInstagram("sohail77")
                .create();

        setContentView(aboutPage);
    }
}
