package com.example.cshare.RequestManager;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.Product;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.NetworkClient;
import com.example.cshare.WebServices.ProductAPI;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * SharedProductsRequestManager is a class that sends api calls and holds shared products data as
 * attributes
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class SharedProductsRequestManager {

    private static SharedProductsRequestManager sharedProductsRequestManager;

    // MutableLiveData object that contains the list of shared products
    private MutableLiveData<List<Product>> productList = new MutableLiveData<>();

    private Retrofit retrofit;
    // Insert API interface dependency here
    private ProductAPI productAPI;


    public SharedProductsRequestManager() {
        /**
         * Constructor that fetch all the list of shared products and store it in the
         * products attributes
         */

        // Define the URL endpoint for the HTTP request.
        retrofit = NetworkClient.getRetrofitClient();
        productAPI = retrofit.create(ProductAPI.class);
    }

    // Getter method
    public MutableLiveData<List<Product>> getProductList(){
        return productList;
    }

    public void getSharedProducts(String token, int userID){
        /**
         * Request to the API to fill the MutableLiveData attribute sharedProductsLiveData with the list of shared products
         */

        Single<List<Product>> products;
        products = productAPI.getProductsByUserID(token, userID);
        products
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new SingleObserver<List<Product>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG,"On start subscription");
                        //sharedProductsLiveData.setValue((List<Product>) ResponseProductList.loading());
                    }

                    @Override
                    public void onSuccess(List<Product> products) {
                        Log.d(Constants.TAG,"All data received");
                        productList.setValue(products);
                        //productList.setValue((List<Product>) ResponseProductList.success(products));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG,"error");
                        //productList.setValue((List<Product>) ResponseProductList.error(new NetworkError(e)));

                    }
                });
     }

    public synchronized static SharedProductsRequestManager getInstance() {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (sharedProductsRequestManager == null) {
            if (sharedProductsRequestManager == null) {
                sharedProductsRequestManager = new SharedProductsRequestManager();
            }
        }
        return sharedProductsRequestManager;
    }
}
