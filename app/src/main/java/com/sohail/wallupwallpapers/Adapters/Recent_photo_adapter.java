package com.sohail.wallupwallpapers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sohail.wallupwallpapers.Models.PhotoModel;
import com.sohail.wallupwallpapers.R;

import java.util.ArrayList;
import java.util.List;

public class Recent_photo_adapter extends RecyclerView.Adapter<Recent_photo_adapter.Recent_photo_holder> {


    private Context context;
    private List<PhotoModel> recentPhotos=new ArrayList<>();

    public Recent_photo_adapter(Context context, List<PhotoModel> recentPhotos) {
        this.context = context;
        this.recentPhotos = recentPhotos;
    }

    @NonNull
    @Override
    public Recent_photo_adapter.Recent_photo_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_photos_item, parent, false);
        return new Recent_photo_adapter.Recent_photo_holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Recent_photo_adapter.Recent_photo_holder holder, int position) {
        PhotoModel photo=recentPhotos.get(position);
        Glide.with(context)
                .load(photo.getUrls().getImage_regular())
                .centerCrop()
                .into(holder.recentImg);
    }


    public void addPhotos(List<PhotoModel> newPhotos) {
        recentPhotos.addAll(newPhotos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recentPhotos.size();
    }

    public class Recent_photo_holder extends RecyclerView.ViewHolder{
        ImageView recentImg;
        public Recent_photo_holder(View itemView) {
            super(itemView);
            recentImg=(itemView.findViewById(R.id.recentImg));
        }
    }
}
