package com.example.cshare.RequestManager.DataSources;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.Models.ApiResponses.ProductListResponse;
import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.ApiError;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.NetworkClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeProductsDataSource extends PageKeyedDataSource<Integer, Product> {

    private static final int FIRST_PAGE = 1;
    private String token;
    private Context context;

    public HomeProductsDataSource(Context context, String token) {
        this.token = token;
        this.context = context;
    }

    // Load the initial data
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Product> callback) {

        NetworkClient.getInstance()
                .getProductAPI()
                .getProducts(token, Constants.AVAILABLE, null, 0, FIRST_PAGE)
                .enqueue(new Callback<ProductListResponse.ApiProductListResponse>() {
                    @Override
                    public void onResponse(Call<ProductListResponse.ApiProductListResponse> call, Response<ProductListResponse.ApiProductListResponse> response) {

                        if (response.isSuccessful()) {
                            Integer key = (response.body().getNext() != null) ? FIRST_PAGE + 1 : null;
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
                    public void onFailure(Call<ProductListResponse.ApiProductListResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    // Load the former data when scrolling up
    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {

        NetworkClient.getInstance()
                .getProductAPI()
                .getProducts(token, Constants.AVAILABLE, null, 0, params.key)
                .enqueue(new Callback<ProductListResponse.ApiProductListResponse>() {
                    @Override
                    public void onResponse(Call<ProductListResponse.ApiProductListResponse> call, Response<ProductListResponse.ApiProductListResponse> response) {

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
                    public void onFailure(Call<ProductListResponse.ApiProductListResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    // Load the further data when scrolling down
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {

        NetworkClient.getInstance()
                .getProductAPI()
                .getProducts(token, Constants.AVAILABLE, null, 0, params.key)
                .enqueue(new Callback<ProductListResponse.ApiProductListResponse>() {
                    @Override
                    public void onResponse(Call<ProductListResponse.ApiProductListResponse> call, Response<ProductListResponse.ApiProductListResponse> response) {
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
                    public void onFailure(Call<ProductListResponse.ApiProductListResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
