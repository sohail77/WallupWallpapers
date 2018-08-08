package com.sohail.wallupwallpapers.Activities;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pkmmte.view.CircularImageView;
import com.sohail.wallupwallpapers.R;

import java.io.File;
import java.io.IOException;

public class ImageViewerActivity extends AppCompatActivity {

    private static final String DIR_NAME ="WallUp Wallpapers";
    private static final String LOG_TAG =ImageViewerActivity.class.getSimpleName() ;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE =2 ;
    ImageView detailImg;
    CircularImageView profileImg;
    TextView username,location;
    Uri imageUri;
    DownloadManager downloadManager;
    Button downloadBtn,setBtn;
    long downloadReference;
    Bitmap bmp;
    private String filename="Wallpaper.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        detailImg=(ImageView)findViewById(R.id.detailImg);
        profileImg=(CircularImageView)findViewById(R.id.profileImg);
        username=(TextView)findViewById(R.id.username);
        downloadBtn=(Button)findViewById(R.id.downloadBtn);
        setBtn=(Button)findViewById(R.id.setBtn);
        location=(TextView)findViewById(R.id.location);
        username.setText(getIntent().getStringExtra("usrname"));
        location.setText(getIntent().getStringExtra("location"));
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("Image"))
                .centerCrop()
                .into(detailImg);

        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("profileImage"))
                .centerCrop()
                .into(profileImg);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition));
            detailImg.setTransitionName("sharedTransition");
        }

        imageUri=Uri.parse(getIntent().getStringExtra("Image"));

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadData(imageUri);
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailImg.buildDrawingCache();
                bmp=detailImg.getDrawingCache();
                settingWallpaper(bmp);
            }
        });


        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant

                return;
            }
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    downloadBtn.setEnabled(true);


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    downloadBtn.setEnabled(false);
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    private void settingWallpaper(Bitmap bmp){
        WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallManager.setBitmap(bmp);
            Toast.makeText(ImageViewerActivity.this, "Wallpaper Set Successfully!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(ImageViewerActivity.this, "Setting WallPaper Failed!!", Toast.LENGTH_SHORT).show();
        }
    }

    private long DownloadData (Uri uri) {


        File direct =
                new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + "/" + DIR_NAME + "/");


        if (!direct.exists()) {
            direct.mkdir();
            Log.d(LOG_TAG, "dir created for first time");
        }
        // Create request for android download manager
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setTitle("Wallpaper");

        //Setting description of request
        request.setDescription("Wallup Wallpapers")
//                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                        File.separator + DIR_NAME + File.separator + filename);

        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //check if the broadcast message is for our enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (referenceId ==downloadReference ) {

                Toast toast = Toast.makeText(ImageViewerActivity.this,
                        "Image Download Complete", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(downloadReceiver);
    }
}
