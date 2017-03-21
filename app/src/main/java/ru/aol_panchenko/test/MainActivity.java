package ru.aol_panchenko.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private static Api api;
    private Retrofit retrofit;
    private List<String> urlList;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private RxJavaCallAdapterFactory rxJavaCallAdapterFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(urlList, this);
        recyclerView.setAdapter(adapter);

        rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        retrofit = new Retrofit.Builder()
                .baseUrl("http://devcandidates.alef.im/list.php/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();

        api = retrofit.create(Api.class);

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
