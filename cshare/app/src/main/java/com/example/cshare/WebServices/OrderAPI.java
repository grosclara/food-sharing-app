package com.example.cshare.WebServices;

import com.example.cshare.Models.ApiResponses.ProductListResponse;
import com.example.cshare.Models.Order;
import com.example.cshare.Models.Product;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderAPI {

    @GET("order/")
    Call<ProductListResponse.ApiProductListResponse> getOrderedProducts(
            @Header("Authorization") String token,
            @Query("page") int page
    );

    @FormUrlEncoded
    @POST("order/")
    Observable<Product> order(
            @Header("Authorization") String token,
            @FieldMap Map<String, Integer> product
    );

    @DELETE("order/{id}/")
    Observable<Product> cancelOrder(
            @Header("Authorization") String token,
            @Path("id") int productID
    );

    @PATCH("order/{id}/")
    Observable<Product> deliverOrder(
            @Header("Authorization") String token,
            @Path("id") int productID
    );
}
