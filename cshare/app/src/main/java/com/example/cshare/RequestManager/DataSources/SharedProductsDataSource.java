package com.example.cshare.RequestManager.DataSources;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.cshare.Models.ApiResponses.ProductListResponse;
import com.example.cshare.Models.Product;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.NetworkClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharedProductsDataSource extends PageKeyedDataSource<Integer, Product> {

        private static final int FIRST_PAGE = 1;

        // Load the initial data
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Product> callback) {

            NetworkClient.getInstance()
                    .getProductAPI()
                    .getProducts(Constants.TOKEN, null, null, 1, FIRST_PAGE)
                    .enqueue(new Callback<ProductListResponse.ApiProductListResponse>() {
                        @Override
                        public void onResponse(Call<ProductListResponse.ApiProductListResponse> call, Response<ProductListResponse.ApiProductListResponse> response) {

                            if(response.body() != null){
                                Integer key = (response.body().getNext() != null) ? FIRST_PAGE+ 1  : null;
                                callback.onResult(response.body().getProductList(), null, key);
                            }

                        }

                        @Override
                        public void onFailure(Call<ProductListResponse.ApiProductListResponse> call, Throwable t) {

                        }
                    });


        }

        // Load the former data when scrolling up
        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {

            NetworkClient.getInstance()
                    .getProductAPI()
                    .getProducts(Constants.TOKEN, null, null, 1, params.key)
                    .enqueue(new Callback<ProductListResponse.ApiProductListResponse>() {
                        @Override
                        public void onResponse(Call<ProductListResponse.ApiProductListResponse> call, Response<ProductListResponse.ApiProductListResponse> response) {

                            if(response.body() != null){
                                Integer key = (params.key > 1) ? params.key - 1 : null;
                                callback.onResult(response.body().getProductList(), key);
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductListResponse.ApiProductListResponse> call, Throwable t) {

                        }
                    });

        }

        // Load the further data when scrolling down
        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {
            NetworkClient.getInstance()
                    .getProductAPI()
                    .getProducts(Constants.TOKEN, null, null, 1, params.key)
                    .enqueue(new Callback<ProductListResponse.ApiProductListResponse>() {
                        @Override
                        public void onResponse(Call<ProductListResponse.ApiProductListResponse> call, Response<ProductListResponse.ApiProductListResponse> response) {

                            if(response.body() != null){
                                Integer key = (response.body().getNext() != null) ? params.key + 1 : null;
                                callback.onResult(response.body().getProductList(), key);
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductListResponse.ApiProductListResponse> call, Throwable t) {

                        }
                    });
        }
    }
