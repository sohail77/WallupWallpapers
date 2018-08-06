package com.sohail.wallupwallpapers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sohail.wallupwallpapers.Models.PhotoModel;
import com.sohail.wallupwallpapers.R;

import java.util.ArrayList;
import java.util.List;

public class Recent_photo_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<PhotoModel> recentPhotos=new ArrayList<>();
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder){
            FooterViewHolder footerViewHolder=(FooterViewHolder)holder;
            footerViewHolder.footerText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "You clicked at Footer View", Toast.LENGTH_SHORT).show();

                }
            });

        }else {
            Recent_photo_holder recent_photo_holder=(Recent_photo_holder) holder;
            PhotoModel photo=recentPhotos.get(position);
            Glide.with(context)
                    .load(photo.getUrls().getImage_regular())
                    .centerCrop()
                    .into(((Recent_photo_holder) holder).recentImg);
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
        TextView footerText;

        public FooterViewHolder(View view) {
            super(view);
            footerText = (TextView) view.findViewById(R.id.recentFooter);
        }
    }
}
