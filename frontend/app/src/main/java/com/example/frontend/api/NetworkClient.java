package com.example.frontend.api;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Class NetworkClient.
 * Public class that instantiates the retrofit object to call to make requests.
 * Retrofit is a library used to implement a REST client in Android.
 * The attributes are the retrofit object and the BASE_URL of the server.
 * The method defined allows to return the retrofit object.
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

/**
 * Public class that instantiates the retrofit object to call to make requests
 */
public class NetworkClient {

    private static Retrofit retrofit;
    // Constant that contains the base url of the remote server
    private static final String BASE_URL = "http://10.0.2.2:8000/api/v1/";

    /*
     * This public static method that takes in param the context of the current activity returns
     * an instance of the Retrofit object anywhere in the application.
     */
    public static Retrofit getRetrofitClient(Context context){
        // If condition to ensure we don't create multiple retrofit instances in a single application
        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Creation of a client
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            //Defining the Retrofit using Builder
            // Retrofit.builder() is used to convert our interface methods to a callable network request which can be executed.
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // This is the only mandatory call on Builder object.
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create()) // Converter which supports converting strings and both primitives and their boxed types to text/plain bodies
                    .addConverterFactory(GsonConverterFactory.create()) // Converter library used to convert response into POJO
                    .build();
        }
        return retrofit;
    }
}
