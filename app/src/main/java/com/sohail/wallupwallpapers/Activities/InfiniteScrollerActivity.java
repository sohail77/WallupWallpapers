package com.sohail.wallupwallpapers.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sohail.wallupwallpapers.Adapters.InfinitePhotoAdapter;
import com.sohail.wallupwallpapers.Adapters.Recent_photo_adapter;
import com.sohail.wallupwallpapers.Api.ApiClient;
import com.sohail.wallupwallpapers.Api.UnsplashService;
import com.sohail.wallupwallpapers.MainActivity;
import com.sohail.wallupwallpapers.Models.PhotoModel;
import com.sohail.wallupwallpapers.R;
import com.sohail.wallupwallpapers.ScrollListener.InfiniteScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfiniteScrollerActivity extends AppCompatActivity {

    public int PAGE_LIMIT=30;
    public static String API_KEY="571250bdd8ee6d1e69c98520dcc78e4505833a0273b97684358f00d19c30fed9";
    public static String HEADER_IMG_URL="https://images.unsplash.com/photo-1532288191429-2093e0783809?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=6583d68060ff2f60484f7edaf88ee276&auto=format&fit=crop&w=3450&q=80";

    private GridLayoutManager gridLayoutManager;
    private InfiniteScrollListener infiniteScrollListener;
    RecyclerView infiniteRv;
    ImageView headerImg;
    InfinitePhotoAdapter adapter;
    boolean isCurated;
    int i,id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infinite_scroller);
        headerImg=(ImageView)findViewById(R.id.headerImg);
        infiniteRv=(RecyclerView)findViewById(R.id.infiniteRv);
        gridLayoutManager = new GridLayoutManager(this, 2);
        infiniteRv.hasFixedSize();
        infiniteRv.setLayoutManager(gridLayoutManager);
        i=getIntent().getExtras().getInt("history");
        id=getIntent().getExtras().getInt("collectionId");
        Glide.with(this)
                .load(HEADER_IMG_URL)
                .centerCrop()
                .into(headerImg);



        infiniteScrollListener = new InfiniteScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(i==1)
                    updateRecentPhotos(page);
                else
                    updateCollectionPhotos(id,page);


            }
        };

        infiniteRv.addOnScrollListener(infiniteScrollListener);
        if(i==1){
            getFirstRecentPhotos();
        }else {
            isCurated = getIntent().getExtras().getBoolean("curated");
            if (!isCurated) {
                getFirstCollectionPhotos(id);
            }
        }

    }


    private void getFirstRecentPhotos(){
        //RECENT PHOTOS FIRST FETCH
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call=service.getRecentPhotos(API_KEY,2,PAGE_LIMIT);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList=response.body();
                adapter = new InfinitePhotoAdapter(InfiniteScrollerActivity.this, photoModelList);
                adapter.notifyDataSetChanged();
                infiniteRv.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });
    }


    private void getFirstCollectionPhotos(int id) {
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call=service.getCollectionPhotos(id,API_KEY,1,PAGE_LIMIT);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList=response.body();
                adapter = new InfinitePhotoAdapter(InfiniteScrollerActivity.this, photoModelList);
                adapter.notifyDataSetChanged();
                infiniteRv.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });
    }

    public void updateRecentPhotos(int page){
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call=service.getRecentPhotos(API_KEY,page,PAGE_LIMIT);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList=response.body();
                adapter.addPhotos(photoModelList);
            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });
    }

    private void updateCollectionPhotos(int id, int page) {
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call=service.getCollectionPhotos(id,API_KEY,page,PAGE_LIMIT);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList=response.body();
                adapter.addPhotos(photoModelList);
            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });

    }
}
