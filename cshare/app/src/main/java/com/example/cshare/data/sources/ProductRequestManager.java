package com.example.cshare.data.sources;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.data.apiresponses.ApiError;
import com.example.cshare.data.apiresponses.EmptyAuthResponse;
import com.example.cshare.data.apiresponses.LoginResponse;
import com.example.cshare.data.apiresponses.ProductResponse;
import com.example.cshare.data.apiresponses.RegistrationResponse;
import com.example.cshare.data.models.Product;
import com.example.cshare.utils.Constants;
import com.example.cshare.webservices.NetworkClient;
import com.example.cshare.webservices.OrderAPI;
import com.example.cshare.webservices.ProductAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that holds several MutableLiveData that contain data related to the products.
 * <p>
 * The class mainly consists of the 5 following live data:
 *  - addProductResponse that contains the a ProductResponse instance
 *  - deleteProductResponse that contains the a ProductResponse instance
 *  - cancelOrderResponse that contains the a ProductResponse instance
 *  - deliverProductResponse that contains the a ProductResponse instance
 *  - orderProductResponse that contains the a ProductResponse instance
 * <p>
 * The defined methods are on the one hand the getters and on the other hand methods that make requests
 * to fill the livedata
 *
 * @see ProductResponse
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */

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

    /**
     * Class constructor
     *
     * @param context
     * @param prefs
     */
    public ProductRequestManager(Context context, PreferenceProvider prefs) {
        /**
         * Constructor that fetch all the list of available products and store it in the
         * products attributes
         */
        // Define the URL endpoint for the HTTP request.
        this.context = context;
        this.productAPI = networkClient.getProductAPI();
        this.orderAPI = networkClient.getOrderApi();
        this.prefs = prefs;
    }

    // Getter methods
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

    /**
     * This method sends a product and a token to the API and sets the data that it receives back in
     * addProductResponse
     * It adds the product given into the API
     **/
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
                            ApiError mError = new ApiError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ApiError.class);
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

    /**
     * This method sends a token and product id to the API and sets the data that it receives back in
     * deleteProductResponse
     * It deletes the product associated to the given product id
     **/
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
                            ApiError mError = new ApiError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ApiError.class);
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

    /**
     * This method sends a token and product id to the API and sets the data that it receives back in
     * orderProductResponse
     * It completes an order of a the product associated to the product id
     **/
    public void order(int productID) {
        Map<String, Integer> productIDMap = new HashMap<>();
        productIDMap.put("product", productID);
        orderAPI
                .order(prefs.getToken(), productIDMap)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            orderProductResponse.setValue(ProductResponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError mError = new ApiError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ApiError.class);
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

    /**
     * This method sends a token and product id to the API and sets the data that it receives back in
     * deliverProductResponse
     * It sets the product status to delivered
     **/
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
                            ApiError mError = new ApiError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ApiError.class);
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

    /**
     * This method sends a token and product id to the API and sets the data that it receives back in
     * cancelOrderResponse
     * It cancels the order related to the given product id
     **/
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
                            ApiError mError = new ApiError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ApiError.class);
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

    /**
     * Method that returns the current repository object if it exists
     * else it creates new repository and returns it
     * @param application
     * @return ProductRequestManager
     */
    public synchronized static ProductRequestManager getInstance(Application application) throws GeneralSecurityException, IOException {

        if (productRequestManager == null) {
            productRequestManager = new ProductRequestManager(application, new PreferenceProvider(application));
        }
        return productRequestManager;
    }

}
