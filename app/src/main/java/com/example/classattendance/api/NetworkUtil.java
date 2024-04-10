package com.example.classattendance.api;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkUtil {
    private static final String ipAddress = "192.168.2.177"; // change here
    private static final String baseUrl = "http://" + ipAddress + ":5280/api/";
    private static volatile NetworkUtil mInstance = null;
    private Retrofit retrofit;
    private NetworkUtil() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .create()))
                .build();
    }
    public static NetworkUtil self() {
        if (mInstance == null)
            mInstance = new NetworkUtil();
        return mInstance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

}