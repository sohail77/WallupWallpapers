package com.sohail.wallupwallpapers.Api;

import com.sohail.wallupwallpapers.Models.FeaturedCollectionModel;
import com.sohail.wallupwallpapers.Models.PhotoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UnsplashService {

    @GET("/photos")
    Call<List<PhotoModel>> getRecentPhotos(@Query("client_id") String apiKey,
                                           @Query("page") int page,
                                           @Query("per_page") int pageLimit);



    @GET("collections/featured")
    Call<List<FeaturedCollectionModel>> getFeaturedCollections(@Query("client_id") String apiKey,
                                                               @Query("page") int page,
                                                               @Query("per_page") int per_page);
}
