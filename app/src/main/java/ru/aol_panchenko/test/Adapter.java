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
    private int screenSize;
    private boolean isTablet;
    private int width;
    private int count;

    public Adapter(List<String> urls, Activity context) {
        this.urls = urls;
        this.context = context;
        // Определяю размер экрана и задаю переменную отвечающую за тип устройства
        screenSize = context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        if(screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE){
            isTablet = true;
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else {
            isTablet = false;
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        count = 0;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
        if(!isTablet){
            // Увеличиваю позицию вручнаю на каждой итерации чтобы картинки не повторялись
            if(position > 0 && position < urls.size()-1){
                count ++;
                position += count;
            }
            String urlImage1 = urls.get(position);
            Picasso.with(context).load(urlImage1).resize((width/2) - 48, (width/2) - 48).into(holder.image1);
            if(position < urls.size()-1){
                String urlImage2 = urls.get(position+1);
                Picasso.with(context).load(urlImage2).resize((width/2) - 48, (width/2) - 48).into(holder.image2);
            }
        }else {
            if(position > 0 && position < urls.size()-2){
                count += 2;
                position += count;
            }
            String urlImage1 = urls.get(position);
            Picasso.with(context).load(urlImage1).resize((width/3) - 64, (width/2) - 64).into(holder.image1);
            if(position < urls.size()-1){
                String urlImage2 = urls.get(position+1);
                Picasso.with(context).load(urlImage2).resize((width/3) - 64, (width/2) - 64).into(holder.image2);
                if(position < urls.size()-2){
                    String urlImage3 = urls.get(position+2);
                    Picasso.with(context).load(urlImage3).resize((width/3) - 64, (width/2) - 64).into(holder.image3);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if(urls == null){
            return 0;
        }else if(isTablet){
            return urls.size()/3;
        }else{
            return urls.size()/2;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image1, image2, image3;

        public ViewHolder(View itemView) {
            super(itemView);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            if(isTablet){
                image3 = (ImageView) itemView.findViewById(R.id.image3);
            }
        }
    }
}
