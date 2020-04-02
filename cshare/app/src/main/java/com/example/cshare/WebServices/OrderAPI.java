package com.example.cshare.WebServices;

import com.example.cshare.Models.Order;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
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
            @Query("client") int userId
    );

    @POST("order/")
    Observable<Order> addOrder(
            @Header("Authorization") String token,
            @Body Order order);

    @DELETE("order/{id}/")
    Observable<ResponseBody> deleteOrderByProductId(
            @Header("Authorization") String token,
            @Path("id") int productId
    );
}