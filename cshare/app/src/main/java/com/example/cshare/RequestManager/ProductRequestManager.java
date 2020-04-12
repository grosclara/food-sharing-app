package com.example.cshare.RequestManager;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;
import com.example.cshare.Models.ProductToPost;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.NetworkClient;
import com.example.cshare.WebServices.OrderAPI;
import com.example.cshare.WebServices.ProductAPI;

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
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductRequestManager {

    private static ProductRequestManager productRequestManager;

    // MutableLiveData object that contains the list of products
    private MutableLiveData<List<Product>> availableProductList = new MutableLiveData<>();
    private MutableLiveData<List<Product>> sharedProductList = new MutableLiveData<>();
    private MutableLiveData<List<Product>> inCartProductList = new MutableLiveData<>();


    private Retrofit retrofit;
    // Insert API interface dependency here
    private ProductAPI productAPI;
    private OrderAPI orderAPI;

    public ProductRequestManager() {
        /**
         * Constructor that fetch all the list of available products and store it in the
         * products attributes
         */
        // Define the URL endpoint for the HTTP request.
        retrofit = NetworkClient.getRetrofitClient();
        productAPI = retrofit.create(ProductAPI.class);
        orderAPI = retrofit.create(OrderAPI.class);

        // Initialize the value of availableProductList
        getAvailableProducts(Constants.TOKEN, Constants.CAMPUS, Constants.STATUS);
        // Initialize the value of inCartProductList
        getInCartProducts(Constants.TOKEN, Constants.USERID);
        // Initialize the value of sharedProductProductList
        getSharedProducts(Constants.TOKEN, Constants.USERID);
    }

    // Getter method
    public MutableLiveData<List<Product>> getAvailableProductList() {
        return availableProductList;
    }
    public MutableLiveData<List<Product>> getInCartProductList() { return inCartProductList; }
    public MutableLiveData<List<Product>> getSharedProductList() { return sharedProductList; }

    public void updateRequestManager() {
        getAvailableProducts(Constants.TOKEN, Constants.CAMPUS, Constants.STATUS);
        getInCartProducts(Constants.TOKEN, Constants.USERID);
        getSharedProducts(Constants.TOKEN, Constants.USERID);
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
                        Log.d(Constants.TAG, "getInCartProducts : on start subscription");
                        // productList.setValue((List<Product>) ResponseProductList.loading());
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        String msg = String.format("inCart : new data received %s", inCartProductList.toString());
                        Log.d(Constants.TAG, msg);
                        //productList.setValue((List<Product>) ResponseProductList.success(products));
                        inCartProductList.setValue(products);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "getInCartProducts : error");
                        Log.d(Constants.TAG, e.getLocalizedMessage());
                        //productList.setValue((List<Product>) ResponseProductList.error(new NetworkError(e)));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "getInCartProducts : All data received");
                    }
                });
    }

    public void getAvailableProducts(String token, String campus, String status) {
        /**
         * Request to the API to fill the MutableLiveData attribute productList with the list of available products
         */

        Observable<List<Product>> products;
        products = productAPI.getAvailableProducts(token, campus, status);
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
                        Log.d(Constants.TAG, "getAvailableProducts : on start subscription");
                        // productList.setValue((List<Product>) ResponseProductList.loading());
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        String msg = String.format("getAvailableProducts : new data received %s", availableProductList.toString());
                        Log.d(Constants.TAG, msg);
                        //productList.setValue((List<Product>) ResponseProductList.success(products));
                        availableProductList.setValue(products);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "getAvailableProducts : error");
                        //productList.setValue((List<Product>) ResponseProductList.error(new NetworkError(e)));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "getAvailableProducts : All data received");
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
                        Log.d(Constants.TAG, "getSharedProducts :  start subscription");
                        // productList.setValue((List<Product>) ResponseProductList.loading());
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        String msg = String.format("getSharedProducts :  new data received %s", sharedProductList.toString());
                        Log.d(Constants.TAG, msg);
                        //productList.setValue((List<Product>) ResponseProductList.success(products));
                        sharedProductList.setValue(products);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "getSharedProducts :  error");
                        //productList.setValue((List<Product>) ResponseProductList.error(new NetworkError(e)));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "getSharedProducts :  All data received");
                    }
                });
    }

    public void addProduct(ProductToPost productToPost, Product productIns) {
        /**
         * Request to the API to post the product taken in param and update the repository
         * @param productToPost
         */

        Observable<ProductToPost> product;
        product = productAPI.addProduct(
                Constants.TOKEN,
                productToPost.getProductPicture(),
                productToPost.getProductName(),
                productToPost.getProductCategory(),
                productToPost.getQuantity(),
                productToPost.getExpirationDate(),
                productToPost.getSupplierID());
        product
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
                .subscribe(new Observer<ProductToPost>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "addProduct : on start subscription");
                    }

                    @Override
                    public void onNext(ProductToPost product) {
                        Log.d(Constants.TAG, "addProduct : Product added successfully");
                        // New product list to which we add the new product
                        List oldAvailable = getAvailableProductList().getValue();
                        List oldShared = getSharedProductList().getValue();
                        oldAvailable.add(0, productIns);
                        oldShared.add(0, productIns);

                        // Wrap this new list in live data
                        availableProductList.setValue(oldAvailable);
                        sharedProductList.setValue(oldShared);

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(Constants.TAG, "addProduct : error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "addProduct : Product received successfully");
                    }
                });
    }

    public void deleteProduct(Product productToDelete) {
        /**
         * Request to the API to delete the product taken in param and update the repository
         * @param productToPost
         */

        Observable<Response<Product>> product;
        product = productAPI.deleteProductById(
                Constants.TOKEN,
                productToDelete.getId());
        Log.d(Constants.TAG, product.toString());
        product
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
                .subscribe(new Observer<Response<Product>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "deleteProduct : on start subscription");
                    }

                    @Override
                    public void onNext(Response<Product> product) {

                        Log.d(Constants.TAG, "deleteProduct : Product deleted successfully");
                        // New product list to which we add the new product
                        List<Product> oldAvailable = getAvailableProductList().getValue();
                        List<Product> oldShared = getSharedProductList().getValue();

                        // Remove the productToDelete from both the shared and available product lists
                        Product pAv = null;
                        ListIterator<Product> itAv = oldAvailable.listIterator();
                        while (itAv.hasNext() && pAv == null) {
                            Product item = itAv.next();
                            if (item.getId() == productToDelete.getId())
                                pAv = item;
                        }
                        Product pSh = null;
                        ListIterator<Product> itSh = oldShared.listIterator();
                        while (itSh.hasNext() && pSh == null) {
                            Product item = itSh.next();
                            if (item.getId() == productToDelete.getId())
                                pSh = item;
                        }

                        oldAvailable.remove(pAv);
                        oldShared.remove(pSh);

                        // Wrap this new list in live data
                        availableProductList.setValue(oldAvailable);
                        sharedProductList.setValue(oldShared);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "deleteProduct : error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "deleteProduct : Deletion completed");
                    }
                });
    }

    public void order(Order request, Map status){
        /**
         * Request to the API to order a product and update its status from available to collected
         */
        Observable<Order> order;
        order = orderAPI.addOrder(Constants.TOKEN, request);
        order
                .flatMap(new Function<Order, Observable<Product>>() {
                    @Override
                    public Observable<Product> apply(Order order) throws Exception {
                        return updateStatus(Constants.TOKEN, request.getProductID(), status);
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

                .subscribe(new Observer<Product>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "addOrder : on start subscription");
                    }

                    @Override
                    public void onNext(Product productIns) {
                        String msg = String.format("addOrder : product status updated and order added");
                        Log.d(Constants.TAG, msg);

                        // Ordered product added to the cart
                        List<Product> oldInCart = getInCartProductList().getValue();
                        oldInCart.add(0, productIns);

                        // Remove the product from the home available list
                        List<Product> oldAvailable = getAvailableProductList().getValue();
                        Product pAv = null;
                        ListIterator<Product> itAv = oldAvailable.listIterator();
                        while (itAv.hasNext() && pAv == null) {
                            Product item = itAv.next();
                            if (item.getId() == productIns.getId())
                                pAv = item;
                        }
                        oldAvailable.remove(pAv);

                        // Wrap these new lists in live data
                        inCartProductList.setValue(oldInCart);
                        availableProductList.setValue(oldAvailable);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "addOrder : error");
                        Log.d(Constants.TAG, e.getLocalizedMessage());
                        //productList.setValue((List<Product>) ResponseProductList.error(new NetworkError(e)));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "getInCartProducts : All data received");
                    }
                });
    }

    public Single<List<Product>> streamFetchProductsFollowingOrders(
            String token, List<Order> orders) {
        Log.d(Constants.TAG, String.valueOf(orders.get(0).getProductID()));
        // Get an Observable of the list
        return Observable
                .fromIterable(orders)
                .concatMap(order -> streamFetchProductFollowingId(
                        token, order.getProductID())
                )
                .toList(); // Items in the list have the same order of initial list
    }

    public Observable<Product> streamFetchProductFollowingId(
            String token, int productID) {
        Log.d(Constants.TAG, String.valueOf(productID));

        return productAPI.getProductById(token, productID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public Observable<Product> updateStatus(
            String token, int productID, Map status) {
        return productAPI.updateProductStatus(token, productID, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public synchronized static ProductRequestManager getInstance() {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (productRequestManager == null) {
            if (productRequestManager == null) {
                productRequestManager = new ProductRequestManager();
            }
        }
        return productRequestManager;
    }

}
