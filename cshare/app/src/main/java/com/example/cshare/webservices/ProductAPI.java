package com.example.cshare.webservices;

import com.example.cshare.data.apiresponses.ProductListResponse;
import com.example.cshare.data.models.Product;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface  ProductAPI {

    @GET("product/{id}/")
    Call<Product> getProductById(
            @Header("Authorization") String token,
            @Path("id") int productID
    );

    @GET("product/")
    Call<ProductListResponse.ApiProductListResponse> getProducts(
            @Header("Authorization") String token,
            @Query("status") String status,
            @Query("category") String category,
            @Query("shared") int shared,
            @Query("page") int page
    );

    @Multipart
    @POST("product/")
    Call<Product> addProduct(
            @Header("Authorization") String token,
            @Part MultipartBody.Part product_picture,
            @Part("name") String name,
            @Part("category") String category,
            @Part("quantity") String quantity,
            @Part("expiration_date") String expiration_date
    );

    @DELETE("product/{id}/")
    Call<Product> deleteProduct(
            @Header("Authorization") String token,
            @Path("id") int productID
    );
}
