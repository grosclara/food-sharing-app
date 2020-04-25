package com.example.cshare.WebServices;

import com.example.cshare.Models.Order;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderAPI {

    @GET("order/")
    Observable<List<Order>> getOrdersByCustomerID(
            @Header("Authorization") String token,
            @Query("client") int userID
    );

    @POST("order/")
    Observable<Order> addOrder(
            @Header("Authorization") String token,
            @Body Order order);

    @DELETE("order/{id}/")
    Observable<Order> deleteOrderByProductId(
            @Header("Authorization") String token,
            @Path("id") int productId
    );
}
