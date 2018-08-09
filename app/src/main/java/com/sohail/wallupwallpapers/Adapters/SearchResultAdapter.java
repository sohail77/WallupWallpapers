package com.sohail.wallupwallpapers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sohail.wallupwallpapers.Models.ResultsArray;
import com.sohail.wallupwallpapers.Models.SearchResultmodel;
import com.sohail.wallupwallpapers.R;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

   Context context;
   private SearchResultmodel searchResultmodels;
   private List<ResultsArray> resultsArrays;

    public SearchResultAdapter(Context context, List<ResultsArray> resultsArray) {
        this.context = context;
        this.resultsArrays = resultsArray;
    }


    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_photos_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {
        ResultsArray searchResultlist=resultsArrays.get(position);
        Glide.with(context)
                .load(searchResultlist.getUrls().getImage_regular())
                .centerCrop()
                .into(holder.imageView);
    }

    public void addPhotos(List<ResultsArray> newPhotos) {
        if(newPhotos!=null) {
            resultsArrays.addAll(newPhotos);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return resultsArrays.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.recentImg);
        }
    }
}
