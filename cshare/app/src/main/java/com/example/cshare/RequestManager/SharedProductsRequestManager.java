package com.example.cshare.RequestManager;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.Product;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.NetworkClient;
import com.example.cshare.WebServices.ProductAPI;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
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

        getSharedProducts(Constants.TOKEN, Constants.USERID);
    }

    public void setProductList(MutableLiveData<List<Product>> productList) {
        this.productList = productList;
    }

    // Getter method
    public MutableLiveData<List<Product>> getProductList() {
        return productList;
    }

    // Update request manager
    public void updateRequestManager(){
        getSharedProducts(Constants.TOKEN, Constants.USERID);
    }

    // Insert product
    public void insert(Product product){
        //Create new product list
        List productList = getProductList().getValue();
        productList.add(0, product);
        // Create live data of this new list
        MutableLiveData<List<Product>> productListLiveData = getProductList();
        productListLiveData.setValue(productList);
        // Set live data in request manager
        setProductList(productListLiveData);
    }

    public void getSharedProducts(String token, int userID) {
        /**
         * Request to the API to fill the MutableLiveData attribute productList with the list of shared products
         */

        Observable<List<Product>> products;
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
                .subscribe(new Observer<List<Product>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "on start subscription");
                        // productList.setValue((List<Product>) ResponseProductList.loading());
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        String msg = String.format("new data received %s", productList.toString());
                        Log.d(Constants.TAG, msg);
                        //productList.setValue((List<Product>) ResponseProductList.success(products));
                        productList.setValue(products);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "error");
                        //productList.setValue((List<Product>) ResponseProductList.error(new NetworkError(e)));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "All data received");
                    }
                });

        /*new SingleObserver<List<Product>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG,"On start subscription");
                        //productList.setValue((List<Product>) ResponseProductList.loading());
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
        }*/
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
