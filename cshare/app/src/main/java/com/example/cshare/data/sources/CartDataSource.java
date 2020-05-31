package com.example.cshare.data.sources;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.data.apiresponses.ProductListResponse;
import com.example.cshare.data.models.Product;
import com.example.cshare.data.apiresponses.ApiError;
import com.example.cshare.utils.Constants;
import com.example.cshare.webservices.NetworkClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.cshare.utils.Constants.FIRST_PAGE;

/**
 *
 * Now here comes the very important thing, the data source of our item from where we will fetch the actual data. And you know that we are using the StackOverflow API.
 *
 * For creating a Data Source we have many options, like ItemKeyedDataSource, PageKeyedDataSource, PositionalDataSource. For this example we are going to use PageKeyedDataSource, as in our API we need to pass the page number for each page that we want to fetch. So here the page number becomes the Key of our page.
 We extended PageKeyedDataSource<Integer, Item> in the above class. Integer here defines the page key, which is in our case a number or an integer. Every time we want a new page from the API we need to pass the page number that we want which is an integer. Item is the item that we will get from the API or that we want to get. We already have a class named Item.
 Then we defined the size of a page which is 50, the initial page number which is 1 and the sitename from where we want to fetch the data. You are free to change these values if you want.
 Then we have 3 overridden methods.
 loadInitials(): This method will load the initial data. Or you can say it will be called once to load the initial data, or first page according to this example.
 loadBefore(): This method will load the previous page.
 loadAfter(): This method will load the next page.

 */
public class CartDataSource extends PageKeyedDataSource<Integer, Product> {

    private String token;
    private Context context;

    public CartDataSource(Context context, String token) {
        this.token = token;
        this.context = context;
    }

    // This will be called once to load the initial data
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Product> callback) {

        NetworkClient.getInstance()
                .getOrderApi()
                .getOrderedProducts(token, FIRST_PAGE)
                .enqueue(new Callback<ProductListResponse>() {
                    @Override
                    public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {

                        if (response.isSuccessful()) {
                            Integer key = (response.body().getNext() != null) ? FIRST_PAGE+ 1  : null;
                            callback.onResult(response.body().getProductList(), null, key);
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError mError = new ApiError();
                            try {
                                mError= gson.fromJson(response.errorBody().string(), ApiError.class);
                                Toast.makeText(context, response.code() + ": " + mError.getDetail(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<ProductListResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    // Load the previous page data when scrolling up
    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {

        NetworkClient.getInstance()
                .getOrderApi()
                .getOrderedProducts(token,params.key)
                .enqueue(new Callback<ProductListResponse>() {
                    @Override
                    public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {

                        //if the current page is greater than one
                        //we are decrementing the page number
                        //else there is no previous page

                        if (response.isSuccessful()) {
                            Integer key = (params.key > 1) ? params.key - 1 : null;
                            callback.onResult(response.body().getProductList(), key);
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError mError = new ApiError();
                            try {
                                mError= gson.fromJson(response.errorBody().string(), ApiError.class);
                                Toast.makeText(context, response.code() + ": " + mError.getDetail(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductListResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    // Load the further data when scrolling down
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {
        NetworkClient.getInstance()
                .getOrderApi()
                .getOrderedProducts(token, params.key)
                .enqueue(new Callback<ProductListResponse>() {
                    @Override
                    public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {

                        if (response.isSuccessful()) {
                            Integer key = (response.body().getNext() != null) ? params.key + 1 : null;
                            callback.onResult(response.body().getProductList(), key);
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError mError = new ApiError();
                            try {
                                mError= gson.fromJson(response.errorBody().string(), ApiError.class);
                                Toast.makeText(context, response.code() + ": " + mError.getDetail(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductListResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

