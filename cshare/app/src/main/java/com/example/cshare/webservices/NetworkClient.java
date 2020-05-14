package com.example.cshare.webservices;

import com.example.cshare.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Singleton class allows the creation of an HTTP client that will be used to make HTTP requests
 * to the server.
 * <p>
 * Retrofit adapts a Java interface to HTTP calls by using annotations on the declared methods
 * to define how requests are made. Create instances using the builder and pass your interface
 * to generate an implementation.
 * @since 1.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class NetworkClient {

    /**
     * Private instance, so that it can be accessed by only by getInstance() method
     */
    private static NetworkClient retrofitClient;
    private Retrofit retrofit;

    /**
     * Private constructor
     */
    private NetworkClient(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_API)
                .client(okHttpClient)
                // Adapt to the Retrofit instance that will allow it to automatically convert its data into Observables
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // A converter which supports converting strings and both primitives
                // and their boxed types to text/plain bodies.
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Synchronized public method to control simultaneous access
     * If instance is null, initialize otherwise, retrieve.
     *
     * @return NetworkClient instance
     */
    public static synchronized NetworkClient getInstance(){
        if(retrofitClient == null){
            retrofitClient = new NetworkClient();
        }
        return retrofitClient;
    }

    /**
     * The Retrofit class generates an implementation of the {@link OrderAPI} interface.
     * @return OrderAPI
     */
    public OrderAPI getOrderApi(){
        return retrofit.create(OrderAPI.class);
    }

    /**
     * The Retrofit class generates an implementation of the {@link ProductAPI} interface.
     * @return ProductAPI
     */
    public ProductAPI getProductAPI(){
        return retrofit.create(ProductAPI.class);
    }

    /**
     * The Retrofit class generates an implementation of the {@link UserAPI} interface.
     * @return UserAPI
     */
    public UserAPI getUserAPI(){
        return retrofit.create(UserAPI.class);
    }

    /**
     * The Retrofit class generates an implementation of the {@link AuthenticationAPI} interface.
     * @return AuthenticationAPI
     */
    public AuthenticationAPI getAuthAPI(){
        return retrofit.create(AuthenticationAPI.class);
    }
}
