package com.example.cshare.WebServices;

import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;
import com.example.cshare.Models.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

public interface DjangoRestApi {

    Retrofit retrofit2 = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    // ---------------------
    // PRODUCT
    // ---------------------

    @GET("product/{id}/")
    Observable<Product> getProductById(
            @Header("Authorization") String token,
            @Path("id") int productId
    );

    @GET("product/")
    Observable<List<Product>> getAvailableProducts(
            @Header("Authorization") String token,
            @Query("campus") String campus,
            @Query("status") String status
    );

    @GET("product/")
    Observable<List<Product>> getGivenProducts(
            @Header("Authorization") String token,
            @Query("supplier") int userId
    );

    @Multipart
    @POST("product/")
    Observable<Product> addProduct(
            @Header("Authorization") String token,
            @Part MultipartBody.Part product_picture,
            @Part("name") String productName,
            @Part("category") String productCategory,
            @Part("quantity") String quantity,
            @Part("expiration_date") String expiration_date,
            @Part("supplier") int supplier
    );

    @FormUrlEncoded
    @PATCH("product/{id}/")
    Call<Product> updateProductStatus(
            @Header("Authorization") String token,
            @Path("id") int id,
            @FieldMap Map<String, String> status
    );

    @DELETE("product/{id}/")
    Call<ResponseBody> deleteProductById(
            @Header("Authorization") String token,
            @Path("id") int productId
    );

    // ---------------------
    // USER
    // ---------------------

    @GET("user/{id}/")
    Observable<User> getUserByID(
            @Header("Authorization") String token,
            @Path("id") int userId);

    @Multipart
    @PUT("user/{id}/")
    Call<User> updateProfileWithPicture(
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

    @Multipart
    @PUT("user/{id}/")
    Call<User> updateProfileWithoutPicture(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Part("first_name") String firstName,
            @Part("last_name") String lastName,
            @Part("room_number") String roomNumber,
            @Part("campus") String campus,
            @Part("email") String email,
            @Part("is_active") Boolean isActive
    );

    @DELETE("user/{id}/")
    Call<ResponseBody> deleteUserById(
            @Header("Authorization") String token,
            @Path("id") int userId
    );

    // ---------------------
    // ORDER
    // ---------------------

    @GET("order/")
    Observable<List<Order>> getOrdersByClient(
            @Header("Authorization") String token,
            @Query("client") int userId
    );

    @POST("order/")
    Call<Order> addOrder(
            @Header("Authorization") String token,
            @Body Order order);

    @DELETE("order/{id}/")
    Call<ResponseBody> deleteOrderByProductId(
            @Header("Authorization") String token,
            @Path("id") int productId
    );

    // AUTHENTICATION REQUESTS

    @GET("rest-auth/user/")
    Call<User> getProfileInfo(
            @Header("Authorization") String token
    );

    @Multipart
    @POST("rest-auth/registration/")
    Call<User> createUserWithPicture(
            @Part MultipartBody.Part profile_picture,
            @Part("first_name") String firstName,
            @Part("last_name") String lastName,
            @Part("room_number") String roomNumber,
            @Part("campus") String campus,
            @Part("email") String email,
            @Part("password1") String password1,
            @Part("password2") String password2
    );

    @POST("rest-auth/registration/")
    Call<User> createUserWithoutPicture(
            @Body User user
    );

    @POST("rest-auth/login/")
    Observable<User> login(@Body User user);

    @POST("rest-auth/logout/")
    Call<ResponseBody> logout(
            @Header("Authorization") String token
    );

    @POST("rest-auth/password/change/")
    Call<User> changePassword(
            @Header("Authorization") String token,
            @Body User user
    );

    @POST("rest-auth/password/reset/")
    Call<User> resetPassword(
            @Body User user
    );
}
