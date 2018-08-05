package com.sohail.wallupwallpapers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.sohail.wallupwallpapers.Adapters.Recent_photo_adapter;
import com.sohail.wallupwallpapers.Api.ApiClient;
import com.sohail.wallupwallpapers.Api.UnsplashService;
import com.sohail.wallupwallpapers.Models.PhotoModel;
import com.sohail.wallupwallpapers.ScrollListener.InfiniteScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static String API_KEY="571250bdd8ee6d1e69c98520dcc78e4505833a0273b97684358f00d19c30fed9";
    public int PAGE_LIMIT=30;
    Recent_photo_adapter adapter;
    private GridLayoutManager gridLayoutManager;
    private InfiniteScrollListener infiniteScrollListener;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recentPhotos);
        gridLayoutManager = new GridLayoutManager(this, 2);

        // TODO: Handle orientation change -- landscape layout

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(gridLayoutManager);

        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call=service.getRecentPhotos(API_KEY,1,PAGE_LIMIT);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList=response.body();
                    adapter = new Recent_photo_adapter(getApplicationContext(), photoModelList);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });

        infiniteScrollListener = new InfiniteScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                updatePhotos(page);

            }
        };
        recyclerView.addOnScrollListener(infiniteScrollListener);

    }

    public void updatePhotos(int page){
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

}
