package com.example.frontend.api;

import com.example.frontend.model.Order;
import com.example.frontend.model.Product;
import com.example.frontend.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
     * Return a call object containing the list of all the available products defined in the db
     *
     * @return
     */
    @GET("product/")
    Call<List<Product>> getAvailableProducts(
            @Header("Authorization") String token,
            @Query("campus") String campus,
            @Query("status") String status
    );

    /**
     * Return a call object containing a single user selected by its id
     *
     * @param userId
     * @return
     */
    @GET("user/{id}/")
    Call<User> getUserByID(
            @Header("Authorization") String token,
            @Path("id") int userId);

    /**
     * Return a call object containing the order to post to the api
     *
     * @param order
     * @return
     */
    @POST("order/")
    Call<Order> addOrder(
            @Header("Authorization") String token,
            @Body Order order);

    /**
     * Return a call object containing the product selected by its id to update in the api
     *
     * @param id
     * @param status
     * @return
     */

    @FormUrlEncoded

    // PATCH allows to update a product modifying only one column in the db
    @PATCH("product/{id}/")
    Call<Product> updateProductStatus(
            @Header("Authorization") String token,
            @Path("id") int id,
            @FieldMap Map<String, String> status
    );

    @FormUrlEncoded
    // PATCH allows to update a product modifying only one column in the db
    @PATCH("user/{id}/")
    Call<User> changeUserCampus(
            @Header("Authorization") String token,
            @Path("id") int id,
            @FieldMap Map<String, String> campus
    );

    /**
     * Return a call object containing a ResponseBody object (the type of the object inside the call is not much important because we won't use it in the method
     *
     * @param product_picture
     * @param productCategory
     * @param productName
     * @param quantity
     * @param expiration_date
     * @param supplier
     * @return
     */

    @Multipart
    // Denotes that the request body is multi-part.
    // Parts should be declared as parameters and annotated with @Part.
    @POST("product/")
    Call<Product> addProduct(
            @Header("Authorization") String token,
            @Part MultipartBody.Part product_picture,
            @Part("name") String productName,
            @Part("category") String productCategory,
            @Part("quantity") String quantity,
            @Part("expiration_date") String expiration_date,
            @Part("supplier") int supplier
    );

    /**
     * Return a call object containing the list of all the products given by the user
     *
     * @return
     */
    @GET("product/")
    Call<List<Product>> getGivenProducts(
            @Header("Authorization") String token,
            @Query("supplier") int userId
    );

    /**
     * Return a call object containing the list of all the orders made by a client
     *
     * @return
     */
    @GET("order/")
    Call<Object> getOrdersByClient(
            @Header("Authorization") String token,
            @Query("client") int userId
    );


    @GET("product/{id}/")
    Call<Product> getProductById(
            @Header("Authorization") String token,
            @Path("id") int productId
    );

    /**
     * Return a call object containing the current user
     *
     * @param profile_picture
     * @param firstName
     * @param lastName
     * @param roomNumber
     * @param email
     * @param campus
     * @param password1
     * @param password2
     * @return
     */

    @Multipart
    // Denotes that the request body is multi-part.
    // Parts should be declared as parameters and annotated with @Part.

    @POST("rest-auth/registration/")
    Call<User> createUser(
            @Part MultipartBody.Part profile_picture,
            @Part("first_name") String firstName,
            @Part("last_name") String lastName,
            @Part("room_number") String roomNumber,
            @Part("campus") String campus,
            @Part("email") String email,
            @Part("password1") String password1,
            @Part("password2") String password2
    );

    @POST("rest-auth/login/")
    Call<Object> login(@Body User user);

    @POST("rest-auth/logout/")
    Call<ResponseBody> logout(
            @Header("Authorization") String token
    );

    @GET("rest-auth/user/")
    Call<User> getProfileInfo(
            @Header("Authorization") String token
    );

    @DELETE("user/{id}/")
    Call<ResponseBody> deleteUserById(
            @Header("Authorization") String token,
            @Path("id") int userId
    );

    @DELETE("order/{id}/")
    Call<ResponseBody> deleteOrderByProductId(
            @Header("Authorization") String token,
            @Path("id") int productId
    );

    @DELETE("product/{id}/")
    Call<ResponseBody> deleteProductById(
            @Header("Authorization") String token,
            @Path("id") int productId
    );

    @Multipart
    // Denotes that the request body is multi-part.
    // Parts should be declared as parameters and annotated with @Part.
    @PUT("user/{id}/")
    Call<User> updateProfile(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Part MultipartBody.Part profile_picture,
            @Part("first_name") String firstName,
            @Part("last_name") String lastName,
            @Part("room_number") String roomNumber,
            @Part("campus") String campus,
            @Part("email") String email,
            @Part("is_active") Boolean isActive
    );

    @POST("rest-auth/password/change/")
    Call<User> changePassword(
            @Header("Authorization") String token,
            @Body User user
    );

    @POST("rest-auth/password/reset/")
    /**
     * uid and token are sent in email after calling /rest-auth/password/reset/
     */
    Call<User> resetPassword(
            @Body User user
    );

}
