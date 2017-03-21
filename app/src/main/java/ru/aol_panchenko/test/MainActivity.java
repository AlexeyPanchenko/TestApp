package ru.aol_panchenko.test;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.aol_panchenko.test.api.Api;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private List<String> urlList;
    private RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlList = new ArrayList<>();

        boolean isTablet;
        // Определяю размер экрана и задаю переменную отвечающую за тип устройства
        int screenSize = getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        if(screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE){
            isTablet = true;
        }else {
            isTablet = false;
        }
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if(!isTablet){
            gridLayoutManager = new GridLayoutManager(this, 2);
        }else {
            gridLayoutManager = new GridLayoutManager(this, 3);
        }
        recyclerView.setLayoutManager(gridLayoutManager);
        Adapter adapter = new Adapter(urlList, width, isTablet, this);
        recyclerView.setAdapter(adapter);

        RxJavaCallAdapterFactory rxJavaCallAdapterFactory =
                RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://devcandidates.alef.im/list.php/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();

        Api api = retrofit.create(Api.class);

        Subscription subscription = api.getImages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        urlList.addAll(strings);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(MainActivity.this, "Error connection", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
