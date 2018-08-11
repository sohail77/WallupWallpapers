package com.sohail.wallupwallpapers.Activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sohail.wallupwallpapers.Adapters.Featured_collection_adapter;
import com.sohail.wallupwallpapers.Adapters.InfinitePhotoAdapter;
import com.sohail.wallupwallpapers.Adapters.Recent_photo_adapter;
import com.sohail.wallupwallpapers.Adapters.SearchResultAdapter;
import com.sohail.wallupwallpapers.Api.ApiClient;
import com.sohail.wallupwallpapers.Api.UnsplashService;
import com.sohail.wallupwallpapers.MainActivity;
import com.sohail.wallupwallpapers.Models.FeaturedCollectionModel;
import com.sohail.wallupwallpapers.Models.PhotoModel;
import com.sohail.wallupwallpapers.Models.ResultsArray;
import com.sohail.wallupwallpapers.Models.SearchResultmodel;
import com.sohail.wallupwallpapers.R;
import com.sohail.wallupwallpapers.ScrollListener.InfiniteScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfiniteScrollerActivity extends AppCompatActivity {

    public int PAGE_LIMIT=30;
    public static String API_KEY="571250bdd8ee6d1e69c98520dcc78e4505833a0273b97684358f00d19c30fed9";

    private GridLayoutManager gridLayoutManager;
    private InfiniteScrollListener infiniteScrollListener;
    RecyclerView infiniteRv;
    InfinitePhotoAdapter adapter;
    SearchResultAdapter searchResultAdapter;
    Featured_collection_adapter featured_collection_adapter,curated_collection_adapter;
    boolean isCurated;
    int i,id;
    TextView headerTxt;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infinite_scroller);
        infiniteRv=(RecyclerView)findViewById(R.id.infiniteRv);
        headerTxt=(TextView)findViewById(R.id.headerTxt);
        progressBar=(ProgressBar)findViewById(R.id.progressBarInfy);
        headerTxt.setText(getIntent().getStringExtra("headerTxt"));
        i=getIntent().getExtras().getInt("history");
        id=getIntent().getExtras().getInt("collectionId");
        if(i==1||i==2||i==5) {
            gridLayoutManager = new GridLayoutManager(this, 2);
        }else
            gridLayoutManager = new GridLayoutManager(this, 1);
        infiniteRv.hasFixedSize();
        infiniteRv.setLayoutManager(gridLayoutManager);

        infiniteScrollListener = new InfiniteScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(i==1)
                    updateRecentPhotos(page);
                else if(i==2) {
                    isCurated = getIntent().getExtras().getBoolean("curated");
                    updateCollectionPhotos(id, page,isCurated);
                }
                else if(i==3)
                    updateFeaturedCollectionList(page);
                else if(i==4)
                    updateCuratedCollectionList(page);
                else {
                    String query=getIntent().getStringExtra("query");
                    updateSearchLists(query,page);
                }
            }
        };

        infiniteRv.addOnScrollListener(infiniteScrollListener);

        if(i==1){
            getFirstRecentPhotos();
        }else if(i==2) {
            isCurated = getIntent().getExtras().getBoolean("curated");
            getFirstCollectionPhotos(id,isCurated);

        }else if(i==3){
            getFirstFeaturedCollectionList();
        }else if(i==4)
            getFirstCuratedCollectionList();
        else{
            String query=getIntent().getStringExtra("query");
            getFirstSearchPhotos(query);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition));
            headerTxt.setTransitionName("sharedTransition");
        }

    }

    private void updateSearchLists(String query, int page) {
        UnsplashService searchService=ApiClient.getClient().create(UnsplashService.class);
        Call<SearchResultmodel> callSearchItems=searchService.getSearchPhotos(API_KEY,query,page,PAGE_LIMIT);
        callSearchItems.enqueue(new Callback<SearchResultmodel>() {
            @Override
            public void onResponse(Call<SearchResultmodel> call, Response<SearchResultmodel> response) {
                List<ResultsArray> resultsArrays=response.body().getResults();
                searchResultAdapter.addPhotos(resultsArrays);
            }

            @Override
            public void onFailure(Call<SearchResultmodel> call, Throwable t) {

            }
        });
    }

    private void getFirstSearchPhotos(String query) {
        UnsplashService searchService=ApiClient.getClient().create(UnsplashService.class);
        Call<SearchResultmodel> callSearchItems=searchService.getSearchPhotos(API_KEY,query,1,PAGE_LIMIT);
        callSearchItems.enqueue(new Callback<SearchResultmodel>() {
            @Override
            public void onResponse(Call<SearchResultmodel> call, Response<SearchResultmodel> response) {
                List<ResultsArray> resultsArray=response.body().getResults();
                searchResultAdapter = new SearchResultAdapter(InfiniteScrollerActivity.this, resultsArray);
                searchResultAdapter.notifyDataSetChanged();
                infiniteRv.setAdapter(searchResultAdapter);
                progressBar.setVisibility(View.GONE);
                infiniteRv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<SearchResultmodel> call, Throwable t) {

            }
        });
    }


    private void getFirstCuratedCollectionList() {
        UnsplashService curated_collection_service=ApiClient.getClient().create(UnsplashService.class);
        Call<List<FeaturedCollectionModel>> call_curated_collection=curated_collection_service.getCuratedCollections(API_KEY,2,20);
        call_curated_collection.enqueue(new Callback<List<FeaturedCollectionModel>>() {
            @Override
            public void onResponse(Call<List<FeaturedCollectionModel>> call, Response<List<FeaturedCollectionModel>> response) {
                List<FeaturedCollectionModel> featuredCollectionModelList=response.body();
                curated_collection_adapter=new Featured_collection_adapter(getApplicationContext(),featuredCollectionModelList);
                curated_collection_adapter.notifyDataSetChanged();
                infiniteRv.setAdapter(curated_collection_adapter);
                progressBar.setVisibility(View.GONE);
                infiniteRv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<FeaturedCollectionModel>> call, Throwable t) {

            }
        });
    }

    private void getFirstFeaturedCollectionList() {
        UnsplashService featured_collection_service=ApiClient.getClient().create(UnsplashService.class);
        Call<List<FeaturedCollectionModel>> call_featured_collection=featured_collection_service.getFeaturedCollections(API_KEY,2,20);
        call_featured_collection.enqueue(new Callback<List<FeaturedCollectionModel>>() {
            @Override
            public void onResponse(Call<List<FeaturedCollectionModel>> call, Response<List<FeaturedCollectionModel>> response) {
                List<FeaturedCollectionModel> featuredCollectionModelList=response.body();
                featured_collection_adapter=new Featured_collection_adapter(getApplicationContext(),featuredCollectionModelList);
                featured_collection_adapter.notifyDataSetChanged();
                infiniteRv.setAdapter(featured_collection_adapter);
                progressBar.setVisibility(View.GONE);
                infiniteRv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<FeaturedCollectionModel>> call, Throwable t) {

            }
        });
    }

    private void updateCuratedCollectionList(int page) {
        UnsplashService curated_collection_service=ApiClient.getClient().create(UnsplashService.class);
        Call<List<FeaturedCollectionModel>> call_curated_collection=curated_collection_service.getCuratedCollections(API_KEY,page,20);
        call_curated_collection.enqueue(new Callback<List<FeaturedCollectionModel>>() {
            @Override
            public void onResponse(Call<List<FeaturedCollectionModel>> call, Response<List<FeaturedCollectionModel>> response) {
                List<FeaturedCollectionModel> featuredCollectionModelList=response.body();
                curated_collection_adapter.addCollections(featuredCollectionModelList);
            }

            @Override
            public void onFailure(Call<List<FeaturedCollectionModel>> call, Throwable t) {

            }
        });
    }

    private void updateFeaturedCollectionList(int page) {
        UnsplashService featured_collection_service=ApiClient.getClient().create(UnsplashService.class);
        Call<List<FeaturedCollectionModel>> call_featured_collection=featured_collection_service.getFeaturedCollections(API_KEY,page,20);
        call_featured_collection.enqueue(new Callback<List<FeaturedCollectionModel>>() {
            @Override
            public void onResponse(Call<List<FeaturedCollectionModel>> call, Response<List<FeaturedCollectionModel>> response) {
                List<FeaturedCollectionModel> featuredCollectionModelList=response.body();
                featured_collection_adapter.addCollections(featuredCollectionModelList);
            }

            @Override
            public void onFailure(Call<List<FeaturedCollectionModel>> call, Throwable t) {

            }
        });
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
                progressBar.setVisibility(View.GONE);
                infiniteRv.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });
    }


    private void getFirstCollectionPhotos(int id,boolean isCurated) {
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call;
        if(!isCurated) {
            call = service.getCollectionPhotos(id, API_KEY, 1, PAGE_LIMIT);
        }else {
            call = service.getCuratedCollectionPhotos(id, API_KEY, 1, PAGE_LIMIT);
        }
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList=response.body();
                adapter = new InfinitePhotoAdapter(InfiniteScrollerActivity.this, photoModelList);
                adapter.notifyDataSetChanged();
                infiniteRv.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                infiniteRv.setVisibility(View.VISIBLE);

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

    private void updateCollectionPhotos(int id, int page,boolean isCurated) {
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call;
        if(!isCurated) {
            call = service.getCollectionPhotos(id, API_KEY, page, PAGE_LIMIT);
        }else {
            call = service.getCuratedCollectionPhotos(id, API_KEY, page, PAGE_LIMIT);
        }        call.enqueue(new Callback<List<PhotoModel>>() {
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
