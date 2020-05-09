package com.example.cshare.RequestManager;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.ApiResponses.EmptyAuthResponse;
import com.example.cshare.Models.ApiResponses.ProductResponse;
import com.example.cshare.Models.ApiResponses.ProductListResponse;
import com.example.cshare.Utils.PreferenceProvider;
import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.NetworkClient;
import com.example.cshare.WebServices.OrderAPI;
import com.example.cshare.WebServices.ProductAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductRequestManager {

    private static ProductRequestManager productRequestManager;

    // MutableLiveData object that contains the list of products
    private MutableLiveData<ProductResponse> addProductResponse = new MutableLiveData<>();
    private MutableLiveData<ProductResponse> deleteProductResponse = new MutableLiveData<>();
    private MutableLiveData<ProductResponse> cancelOrderResponse = new MutableLiveData<>();
    private MutableLiveData<ProductResponse> deliverProductResponse = new MutableLiveData<>();
    private MutableLiveData<ProductResponse> orderProductResponse = new MutableLiveData<>();

    private Context context;
    // Data sources dependencies
    private NetworkClient networkClient = NetworkClient.getInstance();
    private ProductAPI productAPI;
    private OrderAPI orderAPI;
    private PreferenceProvider prefs;

    public ProductRequestManager(Context context, PreferenceProvider prefs) {
        /**
         * Constructor that fetch all the list of available products and store it in the
         * products attributes
         */
        // Define the URL endpoint for the HTTP request.
        this.context = context;
        this.productAPI = networkClient.getProductAPI();
        this.prefs = prefs;
    }

    // Getter method
    public MutableLiveData<ProductResponse> getAddProductResponse() {
        return addProductResponse;
    }

    public MutableLiveData<ProductResponse> getDeleteProductResponse() {
        return deleteProductResponse;
    }

    public MutableLiveData<ProductResponse> getDeliverProductResponse() {
        return deliverProductResponse;
    }

    public MutableLiveData<ProductResponse> getCancelOrderResponse() {
        return cancelOrderResponse;
    }

    public MutableLiveData<ProductResponse> getOrderProductResponse() {
        return orderProductResponse;
    }

    public void addProduct(Product product) {
        productAPI
                .addProduct(prefs.getToken(),
                        product.getProduct_picture_body(),
                        product.getName(),
                        product.getCategory(),
                        product.getQuantity(),
                        product.getExpiration_date())
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            addProductResponse.setValue(ProductResponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ProductResponse.ProductError mError = new ProductResponse.ProductError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ProductResponse.ProductError.class);
                                addProductResponse.setValue(ProductResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteProduct(int productID) {
       productAPI
                .deleteProduct(prefs.getToken(),productID)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            deleteProductResponse.setValue(ProductResponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ProductResponse.ProductError mError = new ProductResponse.ProductError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ProductResponse.ProductError.class);
                                deleteProductResponse.setValue(ProductResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void order(Order order) {
        Map<String, Integer> productIDMap = new HashMap<>();
        productIDMap.put("product", order.getProductID());

        orderAPI
                .order(prefs.getToken(), productIDMap)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            orderProductResponse.setValue(ProductResponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ProductResponse.ProductError mError = new ProductResponse.ProductError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ProductResponse.ProductError.class);
                                orderProductResponse.setValue(ProductResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deliver(int productID) {
        orderAPI
                .deliverOrder(prefs.getToken(),productID)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            deliverProductResponse.setValue(ProductResponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ProductResponse.ProductError mError = new ProductResponse.ProductError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ProductResponse.ProductError.class);
                                deliverProductResponse.setValue(ProductResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void cancelOrder(int productID) {

        orderAPI
                .cancelOrder(prefs.getToken(),productID)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            cancelOrderResponse.setValue(ProductResponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ProductResponse.ProductError mError = new ProductResponse.ProductError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ProductResponse.ProductError.class);
                                cancelOrderResponse.setValue(ProductResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public synchronized static ProductRequestManager getInstance(Application application) throws GeneralSecurityException, IOException {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (productRequestManager == null) {
            productRequestManager = new ProductRequestManager(application, new PreferenceProvider(application));
        }
        return productRequestManager;
    }

}
