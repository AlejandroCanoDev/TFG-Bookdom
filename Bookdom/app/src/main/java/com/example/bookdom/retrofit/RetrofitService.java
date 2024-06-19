package com.example.bookdom.retrofit;

import com.example.bookdom.tools.auth.AuthInterceptor;
import com.example.bookdom.tools.auth.TokenManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://44.220.1.80/";

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            inicializarRetrofit();
        }
        return retrofit;
    }

    public static void inicializarRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(TokenManager.getInstance()))
                .build();

        Gson gson = new GsonBuilder()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static void reiniciarRetrofit() {
        retrofit = null;
        inicializarRetrofit();
    }
}
