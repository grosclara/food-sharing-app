package com.example.frontend.api;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Public class that instantiates the retrofit object to call to make requests
 */
public class NetworkClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:8000/api/v1/";

    public static Retrofit getRetrofitClient(Context context){

        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
