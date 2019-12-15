package com.example.frontend.api;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.frontend.CustomProductsAdapter;
import com.example.frontend.GetAvailableProductsCallbacks;
import com.example.frontend.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Constructor
public class RequestHelper {

    private Retrofit retrofit;
    private DjangoRestApi djangoRestApi;
    private Context context;

    // Constructor called in MainActivity
    public RequestHelper(Context context) {

        this.context = context;

        // Creation of the retrofit object that handles the connection with the db
        retrofit = new Retrofit.Builder()
                .baseUrl(DjangoRestApi.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        djangoRestApi = retrofit.create(DjangoRestApi.class); // Implement the api interface
    }

    public void getAvailableProducts(@Nullable final GetAvailableProductsCallbacks callbacks) {
        /**
         * Void method that will take a callback in its arguments. Once the request is successful,
         * it will load the response in the ArrayList productArrayList and then use the onSuccess
         * method of the callback. This callback is defined in and interface and its on Success and
         * onError methods are overridden in the CollectActivity to populate the listView.
         *
         * @param GetAvailableProductsCallbacks callbacks
         * @see GetAvailableProductsCallbacks
         * @see com.example.frontend.activity.CollectActivity#onCreate(Bundle)
         */

        // Creation of a call object that will contain the response
        Call<List<Product>> callAvailableProducts = djangoRestApi.getAvailableProducts();

        // Asynchronous request
        callAvailableProducts.enqueue(new Callback<List<Product>>() {

            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Product> productArrayList = (ArrayList<Product>) response.body();
                    // Call for the onSuccess method of the callbacks object to process the data
                    // from the productArrayList
                    if (callbacks != null)
                        callbacks.onSuccess(productArrayList);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if (callbacks != null)
                    callbacks.onError(t);
            }
        });
    }

    public void addProduct(Product product) {
        /**
         * Take into param a product and add it to the remote database asynchronously
         *
         * @param Product product
         */

        // Asynchronous request
        Call<Product> call = djangoRestApi.addProduct(product);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    // In case of success, toast "Submit!"
                    Toast.makeText(context, "Submit!", Toast.LENGTH_SHORT);
                } else {
                    Log.d("error", "bad request");
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.d("error", "call to the server failed");
            }
        });
    }

}
