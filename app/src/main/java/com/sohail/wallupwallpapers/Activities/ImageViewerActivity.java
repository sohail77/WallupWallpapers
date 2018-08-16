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
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pkmmte.view.CircularImageView;
import com.sohail.wallupwallpapers.Adapters.InfinitePhotoAdapter;
import com.sohail.wallupwallpapers.Api.ApiClient;
import com.sohail.wallupwallpapers.Api.UnsplashService;
import com.sohail.wallupwallpapers.Models.PhotoModel;
import com.sohail.wallupwallpapers.Models.PhotoStats;
import com.sohail.wallupwallpapers.Models.links;
import com.sohail.wallupwallpapers.R;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageViewerActivity extends AppCompatActivity {

    private static final String DIR_NAME ="WallUp Wallpapers";
    public static String API_KEY="147e5c550ba0b4f96cbc792c659af2239b48580320111d27d7885d21c559236b";
    private static final String LOG_TAG =ImageViewerActivity.class.getSimpleName() ;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE =2 ;
    ImageView detailImg,alertImg;
    CircularImageView profileImg;
    TextView user,location,viewsTxt,downloadTxt,likesTxt;
    Uri imageUri;
    DownloadManager downloadManager;
    Button downloadBtn,setBtn;
    long downloadReference;
    Bitmap bmp;
    private String filename="Wallpaper.jpg";
    int i;
    FABRevealLayout fabRevealLayout;
    CircularProgressDrawable circularProgressDrawable;
    ImageView xBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        detailImg=(ImageView)findViewById(R.id.detailImg);
        profileImg=(CircularImageView)findViewById(R.id.profileImg);
        user=(TextView)findViewById(R.id.user);
        downloadBtn=(Button)findViewById(R.id.downloadBtn);
        setBtn=(Button)findViewById(R.id.setBtn);
        location=(TextView)findViewById(R.id.location);
        fabRevealLayout=(FABRevealLayout)findViewById(R.id.fablayout);
        xBtn=(ImageView) findViewById(R.id.xBtn);
        viewsTxt=(TextView)findViewById(R.id.viewTxt);
        downloadTxt=(TextView)findViewById(R.id.downloadTxt);
        likesTxt=(TextView)findViewById(R.id.likeTxt);
        alertImg=(ImageView)findViewById(R.id.alertImg);
        circularProgressDrawable=new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(this)
                .load(R.drawable.alert)
                .centerCrop()
                .into(alertImg);
        //Getting User Details from Intent
        user.setText(getIntent().getStringExtra("user"));

        String loc=getIntent().getStringExtra("location");
        if(loc == null){
            location.setText("unknown");
        }else
            location.setText(getIntent().getStringExtra("location"));


        i=getIntent().getExtras().getInt("i");


        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("Image"))
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(detailImg);

        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("profileImage"))
                .centerCrop()
                .into(profileImg);

        //GET PHOTO STATS
        //RECENT PHOTOS FIRST FETCH
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<PhotoStats> call=service.getPhotoStats(getIntent().getStringExtra("id"),API_KEY);
        call.enqueue(new Callback<PhotoStats>() {
            @Override
            public void onResponse(Call<PhotoStats> call, Response<PhotoStats> response) {
                PhotoStats photoStats=response.body();
                viewsTxt.setText(String.valueOf(photoStats.getViews().getTotal()));
                downloadTxt.setText(String.valueOf(photoStats.getDownloads().getTotal()));
                likesTxt.setText(String.valueOf(photoStats.getLikes().getTotal()));

            }

            @Override
            public void onFailure(Call<PhotoStats> call, Throwable t) {

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition));
            detailImg.setTransitionName("sharedTransition");
        }

        imageUri=Uri.parse(getIntent().getStringExtra("Image"));

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadData(imageUri);
                fabRevealLayout.revealMainView();
                registerDownload(getIntent().getStringExtra("id"));
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailImg.buildDrawingCache();
                bmp=detailImg.getDrawingCache();
                fabRevealLayout.revealMainView();
                settingWallpaper(bmp,imageUri);

            }
        });

        xBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabRevealLayout.revealMainView();
            }
        });


        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


                return;
            }
        }

        if(i==1) {
            profileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ImageViewerActivity.this, UserProfileActivity.class);
                    i.putExtra("username", getIntent().getStringExtra("username"));
                    i.putExtra("name", getIntent().getStringExtra("user"));
                    i.putExtra("profileImg", getIntent().getStringExtra("profileImage"));
                    i.putExtra("bio", getIntent().getStringExtra("bio"));
                    i.putExtra("instaName", getIntent().getStringExtra("instaName"));
                    i.putExtra("totalPhotos", getIntent().getExtras().getInt("total_photos"));
                    i.putExtra("location", getIntent().getStringExtra("location"));
                    startActivity(i);
                }
            });
        }else
            Log.d("olllllllllllla", getClass().getSimpleName() + " text changed " + i);

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

    private void settingWallpaper(Bitmap bmp,Uri imageUri){
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

    public void registerDownload(String id){
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<links> call=service.getDownloadEndpoint(id,API_KEY);
        call.enqueue(new Callback<links>() {
            @Override
            public void onResponse(Call<links> call, Response<links> response) {
                links res=response.body();
            }

            @Override
            public void onFailure(Call<links> call, Throwable t) {

            }
        });
    }
}
