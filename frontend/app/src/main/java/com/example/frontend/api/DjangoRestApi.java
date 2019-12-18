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
 * Interface that represents the API of the web service in our app
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public interface DjangoRestApi {
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
    Call<Product> addProduct(@Body Product product);

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
    Call<Order> addOrder(@Body Order order);

    /**
     * @return a call object containing the product chosen by its id to update in the api
     * @params id, product
     */
    @PATCH("product/{id}/")
    Call<Product> updateProduct(@Path("id") int id, @Body Product product);

    //@Multipart
    //Denotes that the request body is multi-part.
    // Parts should be declared as parameters and annotated with @Part.
   /* @POST("/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody);*/

    /*@Multipart
    @POST("product/")
    Call<ResponseBody> addProduct(
            @Part MultipartBody.Part product_picture,
            @Part("name") RequestBody name,
            @Part("supplierId") RequestBody supplierId
    );*/


}
