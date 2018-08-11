package com.sohail.wallupwallpapers.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sohail.wallupwallpapers.Activities.ImageViewerActivity;
import com.sohail.wallupwallpapers.Activities.InfiniteScrollerActivity;
import com.sohail.wallupwallpapers.Models.PhotoModel;
import com.sohail.wallupwallpapers.R;

import java.util.ArrayList;
import java.util.List;

public class InfinitePhotoAdapter extends RecyclerView.Adapter<InfinitePhotoAdapter.ViewHolder> {

    Context context;
    public List<PhotoModel> recentList=new ArrayList<>();
    CircularProgressDrawable circularProgressDrawable;

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
    public void onBindViewHolder(@NonNull final InfinitePhotoAdapter.ViewHolder holder, final int position) {
        circularProgressDrawable=new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        PhotoModel photos=recentList.get(position);
        Glide.with(context)
                .load(photos.getUrls().getImage_regular())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, ImageViewerActivity.class);
                i.putExtra("i",1);
                i.putExtra("id",recentList.get(position).getId());
                i.putExtra("profileImage",recentList.get(position).getUser().getProfileImage().getImage_large());
                i.putExtra("Image",recentList.get(position).getUrls().getImage_regular());
                i.putExtra("likes",recentList.get(position).getLikes());
                i.putExtra("user",recentList.get(position).getUser().getName());
                i.putExtra("location",recentList.get(position).getUser().getLocation());
                i.putExtra("username",recentList.get(position).getUser().getUsername());
                i.putExtra("bio",recentList.get(position).getUser().getBio());
                i.putExtra("instaName",recentList.get(position).getUser().getInstaName());
                i.putExtra("total_photos",recentList.get(position).getUser().getTotal_photos());
                holder.imageView.setTransitionName("sharedTransition");
                Pair<View,String> pair=Pair.create((View)holder.imageView,holder.imageView.getTransitionName());
                ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,pair);
                context.startActivity(i,optionsCompat.toBundle());
            }
        });
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
