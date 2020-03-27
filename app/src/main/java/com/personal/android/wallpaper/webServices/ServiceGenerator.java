package com.personal.android.wallpaper.webServices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.personal.android.wallpaper.utils.Constants;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit sRetrofit = null;
    private static  Gson sGson = new  GsonBuilder().create();

    private static HttpLoggingInterceptor sHttpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder sOkHttpClientBuilder = new OkHttpClient.Builder()
            .addInterceptor(sHttpLoggingInterceptor)
            .addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Chain chain) throws IOException {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Authorization", "Client-ID " + Constants.APPLICATION_ID)
                            .build();
                    return chain.proceed(request);
                }
            });

    public static OkHttpClient sOkHttpClient = sOkHttpClientBuilder.build();

    public static <T> T createService(Class<T> serviceClass){
        if(sRetrofit == null){
            sRetrofit = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl(Constants.BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(sGson))
                    .build();
        }
        return sRetrofit.create(serviceClass);
    }
}
