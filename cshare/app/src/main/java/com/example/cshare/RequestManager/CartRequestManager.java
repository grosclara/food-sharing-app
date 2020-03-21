package com.example.cshare.RequestManager;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.NetworkClient;
import com.example.cshare.WebServices.OrderAPI;
import com.example.cshare.WebServices.ProductAPI;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * CartRequestManager is a class that sends api calls and holds products in the shopping cart data as
 * attributes
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class CartRequestManager {

    private static CartRequestManager cartRequestManager;

    // MutableLiveData object that contains the list of products in the cart
    private MutableLiveData<List<Product>> productList = new MutableLiveData<>();

    private Retrofit retrofit;
    // Insert API interface dependency here
    private ProductAPI productAPI;
    private OrderAPI orderAPI;


    public CartRequestManager() {
        /**
         * Constructor that fetch all the list of products in the cart and store it in the
         * products attributes
         */

        // Define the URL endpoint for the HTTP request.
        retrofit = NetworkClient.getRetrofitClient();
        productAPI = retrofit.create(ProductAPI.class);
        orderAPI = retrofit.create(OrderAPI.class);

        // retrieve products in cart
        getInCartProducts(Constants.TOKEN,Constants.USERID);
    }

    // Getter method
    public MutableLiveData<List<Product>> getProductList() {
        return productList;
    }

    public void getInCartProducts(String token, int userID) {
        /**
         * Request to the API to fill the MutableLiveData attribute productList with the list of products
         * present in the cart
         */

        Observable<List<Order>> products;
        products = orderAPI.getOrdersByCustomerID(token, userID);
        products
                .flatMapSingle(new Function<List<Order>, Single<List<Product>>>() {
                    @Override
                    public Single<List<Product>> apply(List<Order> orders) throws Exception {
                        return streamFetchProductsFollowingOrders(token, orders);
                    }
                })

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
    }

    // Create an Observable from a List orders of Order objects.
    // Use concatMap() to map each item emitted by the Observable, to a SingleSource. Inside
    // concatMap(), an asynchronous operation is executed for each item emitted by the Observable.
    // Collected all items in a List products, with Observable.toList()
    // Result: Products has its items in the same order than Orders due to the concatMap() function

    public Single<List<Product>> streamFetchProductsFollowingOrders(
            String token, List<Order> orders) {
        // Get an Observable of the list
        return Observable
                .fromIterable(orders)
                .concatMap(order -> streamFetchProductFollowingId(
                        token, order.getProduct().getId())
                )
                .toList(); // Items in the list have the same order of initial list
    }

    public Observable<Product> streamFetchProductFollowingId(
            String token, int productID) {

        return productAPI.getProductById(token, productID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public synchronized static CartRequestManager getInstance() {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (cartRequestManager == null) {
            if (cartRequestManager == null) {
                cartRequestManager = new CartRequestManager();
            }
        }
        return cartRequestManager;
    }
}
