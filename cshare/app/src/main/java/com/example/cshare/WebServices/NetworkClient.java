package com.example.cshare.WebServices;

import com.example.cshare.Utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    private static NetworkClient retrofitClient;
    private Retrofit retrofit;

    private NetworkClient(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_API)
                .client(okHttpClient)
                // Adapt to the Retrofit instance that will allow it to automatically convert its data into Observables
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized NetworkClient getInstance(){
        if(retrofitClient == null){
            retrofitClient = new NetworkClient();
        }
        return retrofitClient;
    }

    public OrderAPI getOrderApi(){
        return retrofit.create(OrderAPI.class);
    }

    public ProductAPI getProductAPI(){
        return retrofit.create(ProductAPI.class);
    }

    public UserAPI getUserAPI(){
        return retrofit.create(UserAPI.class);
    }

    public AuthenticationAPI getAuthAPI(){
        return retrofit.create(AuthenticationAPI.class);
    }
}
