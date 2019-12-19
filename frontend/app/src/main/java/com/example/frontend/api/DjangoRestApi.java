package com.example.frontend.api;

import com.example.frontend.model.Order;
import com.example.frontend.model.Product;
import com.example.frontend.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that represents the API of the web service in our app.
 * Defines the possible HTTP operations.
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public interface DjangoRestApi {

    /**
     * Return a call object containing the list of all the products defined in the db
     *
     * @return améliorer en filtrant la requete ?????
     */
    @GET("product/")
    Call<List<Product>> getAvailableProducts();

    /**
     * Return a call object containing the product to post to the api
     *
     * @param product
     * @return
     */
    @POST("product/")
    Call<Product> addProduct(@Body Product product);

    /**
     * Return a call object containing a single user selected by its id
     *
     * @param userId
     * @return
     */
    @GET("user/{id}/")
    Call<User> getUserByID(@Path("id") int userId);

    /**
     * Return a call object containing the order to post to the api
     *
     * @param order
     * @return
     */
    @POST("order/")
    Call<Order> addOrder(@Body Order order);

    /**
     * Return a call object containing the product selected by its id to update in the api
     *
     * @param id
     * @param product
     * @return
     */

    // PATCH allows to update a product modifying only one column in the db
    @PATCH("product/{id}/")
    Call<Product> updateProduct(@Path("id") int id, @Body Product product);

}
