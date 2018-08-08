package com.sohail.wallupwallpapers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sohail.wallupwallpapers.Activities.InfiniteScrollerActivity;
import com.sohail.wallupwallpapers.Models.PhotoModel;
import com.sohail.wallupwallpapers.R;

import java.util.ArrayList;
import java.util.List;

public class InfinitePhotoAdapter extends RecyclerView.Adapter<InfinitePhotoAdapter.ViewHolder> {

    Context context;
    public List<PhotoModel> recentList=new ArrayList<>();

    public InfinitePhotoAdapter(Context context, List<PhotoModel> recentPhotos) {
        this.context = context;
        this.recentList = recentPhotos;
    }
    @NonNull
    @Override
    public InfinitePhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_photos_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfinitePhotoAdapter.ViewHolder holder, int position) {
        PhotoModel photos=recentList.get(position);
        Glide.with(context)
                .load(photos.getUrls().getImage_regular())
                .centerCrop()
                .into(holder.imageView);
    }

    public void addPhotos(List<PhotoModel> newPhotos) {
        if(newPhotos!=null) {
            recentList.addAll(newPhotos);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return recentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.recentImg);
        }
    }
}
