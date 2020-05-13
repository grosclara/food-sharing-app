package com.example.cshare.webservices;

import com.example.cshare.data.apiresponses.ProductListResponse;
import com.example.cshare.data.models.Product;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that defines the order-related API endpoints.
 * <p>
 * Provides endpoints to retrieve ordered products, to order a product, to cancel an order or to
 * confirm receipt of the product.
 *
 * @since 1.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public interface OrderAPI {

    @GET("order/")
    Call<ProductListResponse.ApiProductListResponse> getOrderedProducts(
            @Header("Authorization") String token,
            @Query("page") int page
    );

    @FormUrlEncoded
    @POST("order/")
    Call<Product> order(
            @Header("Authorization") String token,
            @FieldMap Map<String, Integer> product
    );

    @DELETE("order/{id}/")
    Call<Product> cancelOrder(
            @Header("Authorization") String token,
            @Path("id") int productID
    );

    @PATCH("order/{id}/")
    Call<Product> deliverOrder(
            @Header("Authorization") String token,
            @Path("id") int productID
    );
}
