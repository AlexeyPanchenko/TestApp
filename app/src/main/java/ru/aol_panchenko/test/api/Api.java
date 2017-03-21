package ru.aol_panchenko.test.api;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface Api {

    @GET(".")
    Observable<List<String>> getImages();
}
