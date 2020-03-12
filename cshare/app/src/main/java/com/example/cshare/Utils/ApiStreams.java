package com.example.cshare.Utils;

//  list all streams related to Django API
// These will all return an Observable so that our controllers (Fragments/Activities) can subscribe to them (Subscriber) and retrieve their data stream

import android.content.Context;
import android.net.Uri;

import com.example.cshare.Models.Product;
import com.example.cshare.WebServices.DjangoRestApi;
import com.example.cshare.WebServices.NetworkClient;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;


public class ApiStreams {

    /*public static Observable<List<Product>> streamFetchAvailableProductsFollowingCampus(
            String token, String campus, String status) {

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        return djangoRestApi.getAvailableProducts(token, campus, status)
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS);
    }*/

    /*public static Observable<User> streamLogin(
            User user) {

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        return djangoRestApi.login(user)
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Product> streamPostProductFollowing(
            Context context,
            String token,
            String imageFilePath,
            Uri fileUri,
            Product product) {

        // Create a file object using file path
        File file = new File(imageFilePath);
        // Create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(context.getContentResolver().getType(fileUri)), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part image = MultipartBody.Part.createFormData("product_picture", file.getName(), requestFile);

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        return djangoRestApi.addProduct(token, image, product.getName(), product.getCategory(), product.getQuantity(), product.getExpiration_date(), product.getSupplier())
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<User> streamFetchUserFollowing(
            String token, int id) {

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        return djangoRestApi.getUserByID(token, id)
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<List<Product>> streamFetchProductsFollowingSupplier(
            String token, int userId) {

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        return djangoRestApi.getGivenProducts(token, userId)
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<Product> streamFetchProductFollowingId(
            String token, int productId) {

        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        return djangoRestApi.getProductById(token, productId)
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<List<Product>> streamFetchCollectedProductsFollowing(String token,
                                                                                  int userId) {
        // Define the URL endpoint for the HTTP operation.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        DjangoRestApi djangoRestApi = retrofit.create(DjangoRestApi.class);

        return djangoRestApi.getOrdersByClient(token, userId)

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
                .timeout(10, TimeUnit.SECONDS);
    }

    // Create an Observable from a List Orders of orders.
    // Use concatMap() to map each item emitted by the Observable, to a SingleSource. Inside
    // concatMap(), an asychronous operation is executed for each item emitted by the Observable.
    // Collected all items in a List products, with Observable.toList()
    // Result: Products has its items in the same order than Orders due to the concatMap() function

    public static Single<List<Product>> streamFetchProductsFollowingOrders(
            String token, List<Order> orders) {
        // Get an Observable of the list

        return Observable
                .fromIterable(orders)
                .concatMap(order -> streamFetchProductFollowingId(
                        token, order.getProduct())
                )
                .toList(); // Items in the list has same order of listA
    }


    // Create a stream that will :
    //     Fetch the user details
    //     Return the user's campus
    //     Fetch products available in the returned campus
    // This stream alone combines the execution of the two previous ones! The links are provided
    // as usual by the Function object. This combination will allow us to call only this stream
    // in our fragment

    public static Observable<List<Product>> streamFetchUserFollowingCampusAndFetchProductsFollowing(
            String token, int userId, String status) {
        return streamFetchUserFollowing(token, userId)
                // With the map() operator, we call our previously created method, which will be
                // executed after data transmission.
                .map(new Function<User, String>()
                        // We have created a method which returns an object of type Function.
                        // This object of type Function is mainly used in ReactiveX to make links
                        // between different methods or Observable within a Stream
                        // User represents to the input variable and
                        // String (campus) the output variable of the function
                {
                    @Override
                    public String apply(User user) throws Exception {
                        return user.getCampus();
                    }
                })
                // Allows, after the emission of an element by an Observable, to transform it into
                // another Observable emitting in its turn one or more elements.
                .flatMap(new Function<String, Observable<List<Product>>>() {
                    @Override
                    public Observable<List<Product>> apply(String campus) throws Exception {
                        return streamFetchAvailableProductsFollowingCampus(token, campus, status);
                    }
                });
    }*/
}
