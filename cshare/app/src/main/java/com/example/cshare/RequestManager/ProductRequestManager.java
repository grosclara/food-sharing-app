package com.example.cshare.RequestManager;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.ApiResponses.ProductResponse;
import com.example.cshare.Models.ApiResponses.ProductListResponse;
import com.example.cshare.Utils.PreferenceProvider;
import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.NetworkClient;
import com.example.cshare.WebServices.OrderAPI;
import com.example.cshare.WebServices.ProductAPI;

import java.io.IOException;
import java.security.GeneralSecurityException;
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

    // Data sources dependencies
    private PreferenceProvider prefs;
    // Insert API interface dependency here
    private ProductAPI productAPI;
    private OrderAPI orderAPI;

    public ProductRequestManager(PreferenceProvider prefs) {
        /**
         * Constructor that fetch all the list of available products and store it in the
         * products attributes
         */
        // Define the URL endpoint for the HTTP request.

        this.prefs = prefs;

        productAPI = NetworkClient.getInstance().getProductAPI();
        orderAPI = NetworkClient.getInstance().getOrderApi();
    }

    // Getter method
    public MutableLiveData<ProductResponse> getAddProductResponse() {
        return addProductResponse;
    }
    public MutableLiveData<ProductResponse> getDeleteProductResponse() {
        return deleteProductResponse;
    }
    public MutableLiveData<ProductResponse> getDeliverProductResponse() { return deliverProductResponse; }
    public MutableLiveData<ProductResponse> getCancelOrderResponse() {
        return cancelOrderResponse;
    }
    public MutableLiveData<ProductResponse> getOrderProductResponse() { return orderProductResponse; }

    public void addProduct(Product product) {
        /**
         * Request to the API to post the product taken in param and update the repository
         * @param productToPost
         */

        Observable<Product> productObservable;
        productObservable = productAPI.addProduct(
                prefs.getToken(),
                product.getProduct_picture_body(),
                product.getName(),
                product.getCategory(),
                product.getQuantity(),
                product.getExpiration_date());

        productObservable
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
                .subscribe(new Observer<Product>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "addProduct : on start subscription");
                        addProductResponse.setValue(ProductResponse.loading());
                    }

                    @Override
                    public void onNext(Product product) {
                        Log.d(Constants.TAG, "addProduct : Product added successfully");

                        // TODO See how to handle the addition of a new product
                        addProductResponse.setValue(ProductResponse.success(product));
                        // New product list to which we add the new product
                        /*List oldAvailable = getAvailableProductList().getValue().getApiProductListResponse().getProductList();
                        List oldShared = getSharedProductList().getValue().getApiProductListResponse().getProductList();
                        oldAvailable.add(0, product);
                        oldShared.add(0, product);*/

                        // Wrap this new list in live data
                        //availableProductList.setValue(ProductListResponse.success(oldAvailable));
                        //sharedProductList.setValue(ProductListResponse.success(oldShared));

                    }

                    @Override
                    public void onError(Throwable e) {
                        addProductResponse.setValue(ProductResponse.error(e));
                        Log.d(Constants.TAG, "addProduct : error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "addProduct : Product received successfully");
                        addProductResponse.setValue(ProductResponse.complete());
                    }
                });
    }

    public void deleteProduct(int productID) {
        /**
         * Request to the API to delete the product taken in param and update the repository
         * @param productToPost
         */
        Observable<Product> observable = productAPI.deleteProduct(prefs.getToken(), productID);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<Product>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "deleteProduct : on start subscription");
                        deleteProductResponse.setValue(ProductResponse.loading());
                    }
                    @Override
                    public void onNext(Product response) {
                        Log.d(Constants.TAG, "deleteProduct : Product deleted successfully");
                        deleteProductResponse.setValue(ProductResponse.success(response));
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "deleteProduct : error");
                        deleteProductResponse.setValue(ProductResponse.error(e));
                    }
                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "deleteProduct : Deletion completed");
                        deleteProductResponse.setValue(ProductResponse.complete());
                    }
                });
    }

    public void order(Order order) {
        /**
         * Request to the API to order a product and update its status from available to collected
         */
        Observable<Product> observable = orderAPI.order(prefs.getToken(), order.getProductID());
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<Product>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "addOrder : on start subscription");
                        orderProductResponse.setValue(ProductResponse.loading());
                    }

                    @Override
                    public void onNext(Product productIns) {
                        // TODO See how to handle this addition
                        /*String msg = String.format("addOrder : product status updated and order added");
                        Log.d(Constants.TAG, msg);
                        orderProductResponse.setValue(ProductResponse.success(productIns));

                        // Ordered product added to the cart
                        List<Product> oldInCart = getInCartProductList().getValue().getApiProductListResponse().getProductList();
                        oldInCart.add(0, productIns);

                        // Remove the product from the home available list
                        List<Product> oldAvailable = getAvailableProductList().getValue().getApiProductListResponse().getProductList();
                        Product pAv = null;
                        ListIterator<Product> itAv = oldAvailable.listIterator();
                        while (itAv.hasNext() && pAv == null) {
                            Product item = itAv.next();
                            if (item.getId() == productIns.getId())
                                pAv = item;
                        }
                        oldAvailable.remove(pAv);*/

                        // Wrap these new lists in live data
                        //inCartProductList.setValue(ProductListResponse.success(oldInCart));
                        //availableProductList.setValue(ProductListResponse.success(oldAvailable));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "addOrder : error");
                        orderProductResponse.setValue(ProductResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "getInCartProducts : All data received");
                        orderProductResponse.setValue(ProductResponse.complete());
                    }
                });
    }

    public void deliver(int productID) {
        Observable<Product> observable = orderAPI.deliverOrder(prefs.getToken(), productID);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<Product>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Deliver : on start subscription");
                        deliverProductResponse.setValue(ProductResponse.loading());
                    }
                    @Override
                    public void onNext(Product product) {
                        Log.d(Constants.TAG, " Deliver : Live data filled");
                        deliverProductResponse.setValue(ProductResponse.success(product));
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Deliver : error");
                        deliverProductResponse.setValue(ProductResponse.error(e));
                    }
                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, " Deliver : Complete");
                        deliverProductResponse.setValue(ProductResponse.complete());
                    }
                });
    }

    public void cancelOrder(int productID) {

        /**
         * Request to the API to order a product and update its status from available to collected
         */
        Observable<Product> productObservable;
        productObservable = orderAPI.cancelOrder(prefs.getToken(), productID);
        productObservable
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS)

                .subscribe(new Observer<Product>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Cancel order : start subscription");
                        cancelOrderResponse.setValue(ProductResponse.loading());
                    }

                    @Override
                    public void onNext(Product productAv) {

                        /*//TODO See how to handle this
                        Log.d(Constants.TAG, "Cancel order : live data filled");
                        cancelOrderResponse.setValue(ProductResponse.success(productAv));

                        // Remove the product from the inCart list and re add it to the available list
                        List<Product> oldAvailable = availableProductList.getValue().getApiProductListResponse().getProductList();
                        List<Product> oldInCart = inCartProductList.getValue().getApiProductListResponse().getProductList();

                        Product pIC = null;
                        ListIterator<Product> itIC = oldInCart.listIterator();
                        while (itIC.hasNext() && pIC == null) {
                            Product item = itIC.next();
                            if (item.getId() == productAv.getId())
                                pIC = item;
                        }
                        oldInCart.remove(pIC);

                        oldAvailable.add(0, productAv);
*/
                        //availableProductList.setValue(ProductListResponse.success(oldAvailable));
                        //inCartProductList.setValue(ProductListResponse.success(oldInCart));

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Cancel order : error");
                        cancelOrderResponse.setValue(ProductResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Cancel order : All data received");
                        cancelOrderResponse.setValue(ProductResponse.complete());
                    }
                });
    }

    public synchronized static ProductRequestManager getInstance(Application application) throws GeneralSecurityException, IOException {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (productRequestManager == null) {
            Log.d("tag", "new instance +1");
            productRequestManager = new ProductRequestManager(new PreferenceProvider(application));
        }
        return productRequestManager;
    }

}
