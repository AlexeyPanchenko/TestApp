package ru.aol_panchenko.test;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private static final String TAG = Adapter.class.getSimpleName();

    private List<String> urls;
    private Context context;
    private boolean isTablet;
    private int width;

    public Adapter(List<String> urls, int width, boolean isTablet, Activity context) {
        this.urls = urls;
        this.width = width;
        this.isTablet = isTablet;
        this.context = context;

    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
        if(!isTablet){
            String urlImage1 = urls.get(position);
            Picasso.with(context).load(urlImage1).resize((width/2) - 32, (width/2) - 32).into(holder.image);
        }else {
            String urlImage1 = urls.get(position);
            Picasso.with(context).load(urlImage1).resize((width/3) - 48, (width/3) - 48).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        if(urls == null){
            return 0;
        }else if(isTablet){
            return urls.size();
        }else{
            return urls.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
