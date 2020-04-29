package com.example.cshare.WebServices;

import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderAPI {

    @GET("order/")
    Observable<List<Product>> getOrderedProducts(
            @Header("Authorization") String token,
            @Query("page") int page
    );

    @POST("order/")
    Observable<Product> order(
            @Header("Authorization") String token,
            @Body int product);

    @DELETE("order/{id}/")
    Observable<Product> deleteOrder(
            @Header("Authorization") String token,
            @Path("id") int productID
    );

    @PATCH("order/{id}/")
    Observable<Product> deliverOrder(
            @Header("Authorization") String token,
            @Path("id") int productID
    );
}
