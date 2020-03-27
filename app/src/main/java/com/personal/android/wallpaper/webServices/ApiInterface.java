package com.personal.android.wallpaper.webServices;

import com.personal.android.wallpaper.models.Collection;
import com.personal.android.wallpaper.models.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("photos?page=1&per_page=50")
    Call<List<Photo>> getPhotos();

    @GET("collections/featured?page=1&per_page=50")
    Call<List<Collection>> getCollections();

    @GET("collections/{id}")
    Call<Collection> getInformationOfCollection(@Path("id") int id);

    @GET("collections/{id}/photos?page=1&per_page=50")
    Call<List<Photo>> getPhotosOfCollection(@Path("id") int id);

    @GET("photos/{id}")
    Call<Photo> getPhoto(@Path("id")  String id);
}
