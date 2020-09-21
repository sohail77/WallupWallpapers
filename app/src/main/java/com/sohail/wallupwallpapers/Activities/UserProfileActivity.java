package com.sohail.wallupwallpapers.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pkmmte.view.CircularImageView;
import com.sohail.wallupwallpapers.Adapters.ProfilePhotosAdapter;
import com.sohail.wallupwallpapers.Api.ApiClient;
import com.sohail.wallupwallpapers.Api.UnsplashService;
import com.sohail.wallupwallpapers.Models.PhotoModel;
import com.sohail.wallupwallpapers.R;
import com.sohail.wallupwallpapers.ScrollListener.InfiniteScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    public int PAGE_LIMIT = 30;
    public static String API_KEY = "147e5c550ba0b4f96cbc792c659af2239b48580320111d27d7885d21c559236b";
    CircularImageView profileImg;
    TextView name, location, total_photos, instName;
    String username;
    ProfilePhotosAdapter adapter;
    RecyclerView rv;
    private GridLayoutManager gridLayoutManager;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        profileImg = (CircularImageView) findViewById(R.id.userImg);
        name = (TextView) findViewById(R.id.profileUserName);
        location = (TextView) findViewById(R.id.profileLocation);
        total_photos = (TextView) findViewById(R.id.profilePhotos);
        instName = (TextView) findViewById(R.id.profileInsta);
        progressBar = (ProgressBar) findViewById(R.id.progressBarUser);

        rv = (RecyclerView) findViewById(R.id.profileRv);
        gridLayoutManager = new GridLayoutManager(this, 1);
        rv.hasFixedSize();
        rv.setLayoutManager(gridLayoutManager);

        Glide.with(this)
                .load(getIntent().getStringExtra("profileImg"))
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(profileImg);


        name.setText(getIntent().getStringExtra("name"));

        String loc = getIntent().getStringExtra("location");
        if (loc == null) {
            location.setText("unknown");
        } else
            location.setText(getIntent().getStringExtra("location"));

        loc = getIntent().getStringExtra("instaName");
        if (loc == null) {
            instName.setText("Instagram : Unknown");
        } else {
            instName.setText("Instagram : " + getIntent().getStringExtra("instaName"));
        }
        total_photos.setText("Total Photos : " + String.valueOf(getIntent().getExtras().getInt("totalPhotos")));
        username = getIntent().getStringExtra("username");

        //USER PHOTOS FIRST FETCH
        UnsplashService service = ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call = service.getUserProfile(username, API_KEY, 1, PAGE_LIMIT);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList = response.body();
                adapter = new ProfilePhotosAdapter(UserProfileActivity.this, photoModelList);
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });

        InfiniteScrollListener infiniteScrollListener = new InfiniteScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                updatephotos(username, page);
            }
        };

        rv.addOnScrollListener(infiniteScrollListener);

    }

    private void updatephotos(String username, int page) {
        UnsplashService service = ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call = service.getUserProfile(username, API_KEY, page, PAGE_LIMIT);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList = response.body();
                adapter.addPhotos(photoModelList);
            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });
    }
}
