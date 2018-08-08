package com.sohail.wallupwallpapers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sohail.wallupwallpapers.Activities.InfiniteScrollerActivity;
import com.sohail.wallupwallpapers.Models.FeaturedCollectionModel;
import com.sohail.wallupwallpapers.R;


import java.util.ArrayList;
import java.util.List;

public class Featured_collection_adapter extends RecyclerView.Adapter<Featured_collection_adapter.ViewHolder> {

    Context context;
    List<FeaturedCollectionModel> featuredCollectionModels=new ArrayList<>();

    public Featured_collection_adapter(Context context, List<FeaturedCollectionModel> featuredCollectionModels) {
        this.context = context;
        this.featuredCollectionModels = featuredCollectionModels;
    }

    @NonNull
    @Override
    public Featured_collection_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_collection_item, parent, false);
        return new Featured_collection_adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Featured_collection_adapter.ViewHolder holder, final int position) {
        final FeaturedCollectionModel collectionModel=featuredCollectionModels.get(position);
        Glide.with(context)
                .load(collectionModel.getCoverPhoto().getUrls().getImage_regular())
                .centerCrop()
                .into(holder.FeaturedcollectionImg);
        holder.collectionName.setText(featuredCollectionModels.get(position).getTitle());

        holder.FeaturedcollectionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, InfiniteScrollerActivity.class);
                i.putExtra("collectionId",featuredCollectionModels.get(position).getId());
                i.putExtra("history",2);
                i.putExtra("coverImg",featuredCollectionModels.get(position).getCoverPhoto().getUrls().getImage_regular());
                i.putExtra("curated",featuredCollectionModels.get(position).isCurated());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return featuredCollectionModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView FeaturedcollectionImg;
        TextView collectionName;
        public ViewHolder(View itemView) {
            super(itemView);
            FeaturedcollectionImg=itemView.findViewById(R.id.featured_collection_img);
            collectionName=itemView.findViewById(R.id.collectionName);
        }
    }
}
