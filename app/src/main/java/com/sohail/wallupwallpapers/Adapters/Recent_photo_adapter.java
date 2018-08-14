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
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sohail.wallupwallpapers.Activities.ImageViewerActivity;
import com.sohail.wallupwallpapers.Activities.InfiniteScrollerActivity;
import com.sohail.wallupwallpapers.Models.PhotoModel;
import com.sohail.wallupwallpapers.R;

import java.util.ArrayList;
import java.util.List;

public class Recent_photo_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<PhotoModel> recentPhotos=new ArrayList<>();
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    public String link="https://images.unsplash.com/photo-1428908728789-d2de25dbd4e2?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=d00f965d7f845968dd35c3590094149f&auto=format&fit=crop&w=2250&q=80";
    CircularProgressDrawable circularProgressDrawable;

    public Recent_photo_adapter(Context context, List<PhotoModel> recentPhotos) {
        this.context = context;
        this.recentPhotos = recentPhotos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_photos_item, parent, false);
            return new Recent_photo_holder(itemView);
        }else if(viewType==TYPE_FOOTER){
            View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_phoyos_footer_item,parent,false);
            return new FooterViewHolder(itemView);
        }else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        circularProgressDrawable=new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        if (holder instanceof FooterViewHolder){
            FooterViewHolder footerViewHolder=(FooterViewHolder)holder;

            Glide.with(context)
                    .load(link)
                    .centerCrop()
                    .into(footerViewHolder.backImg);


            footerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, InfiniteScrollerActivity.class);
                    intent.putExtra("history",1);
                    intent.putExtra("headerTxt","Recent Photos");
                    context.startActivity(intent);

                }
            });

        }else {
            final Recent_photo_holder recent_photo_holder=(Recent_photo_holder) holder;
            PhotoModel photo=recentPhotos.get(position);
            Glide.with(context)
                    .load(photo.getUrls().getImage_regular())
                    .centerCrop()
                    .placeholder(circularProgressDrawable)
                    .into(((Recent_photo_holder) holder).recentImg);

            recent_photo_holder.recentImg.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(context, ImageViewerActivity.class);
                    i.putExtra("i",1);
                    i.putExtra("id",recentPhotos.get(position).getId());
                    i.putExtra("profileImage",recentPhotos.get(position).getUser().getProfileImage().getImage_large());
                    i.putExtra("Image",recentPhotos.get(position).getUrls().getFull());
                    i.putExtra("likes",recentPhotos.get(position).getLikes());
                    i.putExtra("user",recentPhotos.get(position).getUser().getName());
                    i.putExtra("username",recentPhotos.get(position).getUser().getUsername());
                    i.putExtra("location",recentPhotos.get(position).getUser().getLocation());
                    i.putExtra("bio",recentPhotos.get(position).getUser().getBio());
                    i.putExtra("instaName",recentPhotos.get(position).getUser().getInstaName());
                    i.putExtra("total_photos",recentPhotos.get(position).getUser().getTotal_photos());
                    recent_photo_holder.recentImg.setTransitionName("sharedTransition");
                    Pair<View,String> pair=Pair.create((View)recent_photo_holder.recentImg,recent_photo_holder.recentImg.getTransitionName());
                    ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,pair);
                    context.startActivity(i,optionsCompat.toBundle());
                }
            });
        }
    }

//    @Override
//    public void onBindViewHolder(@NonNull Recent_photo_adapter.Recent_photo_holder holder, int position) {
//        PhotoModel photo=recentPhotos.get(position);
//        Glide.with(context)
//                .load(photo.getUrls().getImage_regular())
//                .centerCrop()
//                .into(holder.recentImg);
//    }


    public void addPhotos(List<PhotoModel> newPhotos) {
        if(newPhotos!=null) {
            recentPhotos.addAll(newPhotos);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == recentPhotos.size()-1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
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

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        ImageView footerText,backImg;

        public FooterViewHolder(View view) {
            super(view);
            backImg=view.findViewById(R.id.backImg);
            footerText = view.findViewById(R.id.recentFooter);
        }
    }
}
