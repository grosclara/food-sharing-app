package com.example.frontend.api;

import com.example.frontend.model.Order;
import com.example.frontend.model.Product;
import com.example.frontend.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that represents the API of the web service in our app
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public interface DjangoRestApi {

    /** Base Url of the API */
    String baseUrl = "http://10.0.2.2:8000/api/v1/";

    /**
     * @return a call object containing a list of products
     */
    @GET("product/")
    Call<List<Product>> getAvailableProducts(
//            @Query("sort") String sort,
//            @Query("order") String order
    );

    /**
     * @param product
     * @return a call object containing the product to post to the api
     */
    @POST("product/")
    Call<Product> addProduct(@Body Product product );

    /**
     * @param userId
     * @return a call object containing a single user chosen by its id
     */
    @GET("user/{id}/")
    Call<User> getUserByID(@Path("id") int userId);

    /**
     * @param order
     * @return a call object containing the order to post to the api
     */
    @POST("order/")
    Call<Order> addOrder(@Body Order order );


}
