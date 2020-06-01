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
 * Data source of our available product item from where we will fetch the actual data.
 * <p>
 * As in our API we need to pass the page number for each page that we want to fetch, this class
 * extends the We extended PageKeyedDataSource<Integer, Product> class.
 * Integer here defines the page key, which is in our case a number or an integer.
 * Every time we want a new page from the API we need to pass the page number that we want which
 * is an integer. Product is the item that we will get from the API.
 */
public class HomeDataSource extends PageKeyedDataSource<Integer, Product> {

    private String token;
    private Context context;

    /**
     * Class constructor
     *
     * @param context
     * @param token Token of the user
     */
    public HomeDataSource(Context context, String token) {
        this.token = token;
        this.context = context;
    }

    /*
     * This method is responsible to load the data initially  when app screen is launched for the
     * first time. We are fetching the first page data from the api and passing it via the callback
     * method to the UI.
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Product> callback) {
        Log.d(Constants.TAG, "LOAD INITIAL");

        NetworkClient.getInstance()
                .getProductAPI()
                .getProducts(token, Constants.AVAILABLE, null, 0, FIRST_PAGE)
                .enqueue(new Callback<ProductListResponse>() {
                    @Override
                    public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {

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
                    public void onFailure(Call<ProductListResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*
     * This method is responsible for the subsequent call to load the data page wise. This method
     * is executed in the background thread. We are fetching the previous page data from the api
     * and passing it via the callback method to the UI. The "params.key" variable will have
     * the updated value.
     */
    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {
        Log.d(Constants.TAG, "LOAD BEFORE");

        NetworkClient.getInstance()
                .getProductAPI()
                .getProducts(token, Constants.AVAILABLE, null, 0, params.key)
                .enqueue(new Callback<ProductListResponse>() {
                    @Override
                    public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {

                        if (response.isSuccessful()) {
                            // If the response has previous page, decrementing the next page number
                            Integer key = (response.body().getPrevious() != null) ? params.key - 1 : null;
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

    /*
     * This method is responsible for the subsequent call to load the data page wise. This method
     * is executed in the background thread. We are fetching the next page data from the api
     * and passing it via the callback method to the UI. The "params.key" variable will have
     * the updated value.
     */
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {

        NetworkClient.getInstance()
                .getProductAPI()
                .getProducts(token, Constants.AVAILABLE, null, 0, params.key)
                .enqueue(new Callback<ProductListResponse>() {
                    @Override
                    public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                        if (response.isSuccessful()) {
                            // If the response has next page, incrementing the next page number
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
