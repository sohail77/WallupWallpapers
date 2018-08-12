package com.sohail.wallupwallpapers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.github.florent37.expectanim.ExpectAnim;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.sohail.wallupwallpapers.Activities.AboutActivity;
import com.sohail.wallupwallpapers.Activities.InfiniteScrollerActivity;
import com.sohail.wallupwallpapers.Activities.OnBoarding;
import com.sohail.wallupwallpapers.Adapters.Featured_collection_adapter;
import com.sohail.wallupwallpapers.Adapters.Recent_photo_adapter;
import com.sohail.wallupwallpapers.Api.ApiClient;
import com.sohail.wallupwallpapers.Api.UnsplashService;
import com.sohail.wallupwallpapers.Models.FeaturedCollectionModel;
import com.sohail.wallupwallpapers.Models.PhotoModel;
import com.sohail.wallupwallpapers.ScrollListener.InfiniteScrollListener;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.github.florent37.expectanim.core.Expectations.alpha;
import static com.github.florent37.expectanim.core.Expectations.height;
import static com.github.florent37.expectanim.core.Expectations.leftOfParent;
import static com.github.florent37.expectanim.core.Expectations.scale;
import static com.github.florent37.expectanim.core.Expectations.topOfParent;

public class MainActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    public static String API_KEY="571250bdd8ee6d1e69c98520dcc78e4505833a0273b97684358f00d19c30fed9";
    public static String BACKGROUND_IMG_URL="https://images.unsplash.com/photo-1532288191429-2093e0783809?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=6583d68060ff2f60484f7edaf88ee276&auto=format&fit=crop&w=3450&q=80";
    public int PAGE_LIMIT=30;
    Recent_photo_adapter adapter;
    private GridLayoutManager gridLayoutManager;
    private InfiniteScrollListener infiniteScrollListener;
    RecyclerView recyclerView;
    RecyclerView featured_collection_rv,curated_collection_rv;
    Featured_collection_adapter featured_collection_adapter,curated_collection_adapter;
    MaterialSearchBar searchBar;
    ImageView backgroundImg,aboutIcon;
    TextView seeAllTxt,seeAllTxt2;
    ProgressBar progressBar,progressBar2,progressBar3;


    @BindView(R.id.mainTxt)
    View mainTxt;

    @BindView(R.id.background)
    View backbground;

    @BindView(R.id.scrim)
    View scrim;

    @BindView(R.id.scrollview)
    NestedScrollView scrollView;

    @BindDimen(R.dimen.height)
    int height;

    private ExpectAnim expectAnimMove;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SharedPreferences settings=getSharedPreferences("prefs",0);
        boolean firstRun=settings.getBoolean("firstRun",false);
        if(!firstRun)//if running for first time
        //Splash will load for first time
        {
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("firstRun",true);
            editor.apply();
            Intent i=new Intent(MainActivity.this,OnBoarding.class);
            startActivity(i);
            finish();
        }

        backgroundImg=(ImageView)findViewById(R.id.background_img);
        recyclerView = (RecyclerView) findViewById(R.id.recentPhotos);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar2=(ProgressBar)findViewById(R.id.progressBar2);
        progressBar3=(ProgressBar)findViewById(R.id.progressBar3);
        aboutIcon=(ImageView)findViewById(R.id.aboutIcon);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(gridLayoutManager);
        ViewCompat.setNestedScrollingEnabled(recyclerView,false);
        featured_collection_rv=(RecyclerView)findViewById(R.id.featuredCollectionrv);
        featured_collection_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        curated_collection_rv=(RecyclerView)findViewById(R.id.curatedCollectionrv);
        curated_collection_rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);

        seeAllTxt=(TextView)findViewById(R.id.seeAllTxt);
        seeAllTxt2=(TextView)findViewById(R.id.seeAllTxt2);


        Glide.with(getApplicationContext())
                .load(BACKGROUND_IMG_URL)
                .centerCrop()
                .into(backgroundImg);

        this.expectAnimMove = new ExpectAnim()
                .expect(searchBar)
                .toBe(
                        topOfParent().withMarginDp(30),
                        leftOfParent().withMarginDp(10)

                )

                .expect(scrim)
                .toBe(
                        alpha(0f)
                )

                .expect(mainTxt)
                .toBe(
                        alpha(0f)
                )


                .expect(backgroundImg)
                .toBe(
                        alpha(0f)

                )

                .expect(aboutIcon)
                .toBe(
                        alpha(0f)
                )

                .expect(backbground)
                .toBe(
                        height(height).withGravity(Gravity.LEFT, Gravity.TOP)
                )

                .toAnimation();

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                final float percent = (scrollY * 1f) / v.getMaxScrollAmount();
                expectAnimMove.setPercent(percent);
            }
        });

        //RECENT PHOTOS FIRST FETCH
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call=service.getRecentPhotos(API_KEY,1,PAGE_LIMIT);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList=response.body();
                    adapter = new Recent_photo_adapter(MainActivity.this, photoModelList);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    progressBar3.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });

//        infiniteScrollListener = new InfiniteScrollListener(gridLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                updatePhotos(page);
//
//            }
//        };
//        recyclerView.addOnScrollListener(infiniteScrollListener);

        //FEATURED COLLECTIONS FIRST FETCH
        UnsplashService featured_collection_service=ApiClient.getClient().create(UnsplashService.class);
        Call<List<FeaturedCollectionModel>> call_featured_collection=featured_collection_service.getFeaturedCollections(API_KEY,1,20);
        call_featured_collection.enqueue(new Callback<List<FeaturedCollectionModel>>() {
            @Override
            public void onResponse(Call<List<FeaturedCollectionModel>> call, Response<List<FeaturedCollectionModel>> response) {
                List<FeaturedCollectionModel> featuredCollectionModelList=response.body();
                featured_collection_adapter=new Featured_collection_adapter(getApplicationContext(),featuredCollectionModelList);
                featured_collection_adapter.notifyDataSetChanged();
                featured_collection_rv.setAdapter(featured_collection_adapter);
                progressBar.setVisibility(View.GONE);
                featured_collection_rv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<FeaturedCollectionModel>> call, Throwable t) {

            }
        });

        //CURATED COLLECTIONS FIRST FETCH
        UnsplashService curated_collection_service=ApiClient.getClient().create(UnsplashService.class);
        Call<List<FeaturedCollectionModel>> call_curated_collection=curated_collection_service.getCuratedCollections(API_KEY,1,20);
        call_curated_collection.enqueue(new Callback<List<FeaturedCollectionModel>>() {
            @Override
            public void onResponse(Call<List<FeaturedCollectionModel>> call, Response<List<FeaturedCollectionModel>> response) {
                List<FeaturedCollectionModel> featuredCollectionModelList=response.body();
                curated_collection_adapter=new Featured_collection_adapter(getApplicationContext(),featuredCollectionModelList);
                curated_collection_adapter.notifyDataSetChanged();
                curated_collection_rv.setAdapter(curated_collection_adapter);
                progressBar2.setVisibility(View.GONE);
                curated_collection_rv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<FeaturedCollectionModel>> call, Throwable t) {

            }
        });

        seeAllTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, InfiniteScrollerActivity.class);
                intent.putExtra("history",3);
                intent.putExtra("headerTxt","Featured Collections");
                startActivity(intent);
            }
        });

        seeAllTxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, InfiniteScrollerActivity.class);
                intent.putExtra("history",4);
                intent.putExtra("headerTxt","Curated Collections");
                startActivity(intent);
            }
        });

        aboutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

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

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchBar.clearSuggestions();
        Log.d("olllllllllllla", getClass().getSimpleName() + " text changed " + searchBar.getText());
        Intent i=new Intent(MainActivity.this,InfiniteScrollerActivity.class);
        i.putExtra("history",5);
        i.putExtra("headerTxt","Results for : " +searchBar.getText());
        i.putExtra("query",searchBar.getText());
        startActivity(i);
    }

    @Override
    public void onButtonClicked(int buttonCode) {

        switch (buttonCode){
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                break;
        }

    }
}
