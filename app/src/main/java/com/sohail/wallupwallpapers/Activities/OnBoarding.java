package com.sohail.wallupwallpapers.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.sohail.wallupwallpapers.MainActivity;
import com.sohail.wallupwallpapers.R;

import java.util.ArrayList;
import java.util.List;

public class OnBoarding extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Welcome to WallUp", "Powered By Unsplash", R.mipmap.ic_launcher_round);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Hi-res Photos", "Free (Do what ever you want) Photos", R.drawable.photos);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Download", "Download and Set as Wallpaper", R.drawable.save);

        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);


        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.grey_200);
            //page.setTitleTextSize(dpToPixels(12, this));
            //page.setDescriptionTextSize(dpToPixels(8, this));
            //page.setIconLayoutParams(width, height, marginTop, marginLeft, marginRight, marginBottom);
        }

        setFinishButtonTitle("Finish");
        showNavigationControls(true);
        setGradientBackground();

        //set the button style you created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button));
        }



        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
        finish();

    }
}
