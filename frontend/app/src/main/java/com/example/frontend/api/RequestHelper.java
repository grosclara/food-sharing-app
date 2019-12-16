package com.example.frontend.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Public class that instantiates the retrofit object to call to make requests
 */
public class RequestHelper {

    private Retrofit retrofit;
    public DjangoRestApi djangoRestApi;

    // Constructor called in MainActivity
    public RequestHelper() {

        // Creation of the retrofit object that handles the connection with the db
        retrofit = new Retrofit.Builder()
                .baseUrl(djangoRestApi.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        djangoRestApi = retrofit.create(DjangoRestApi.class); // Implement the api interface
    }
}
