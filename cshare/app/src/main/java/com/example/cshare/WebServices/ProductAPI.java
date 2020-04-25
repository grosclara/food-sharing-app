package com.example.cshare.WebServices;

import com.example.cshare.Models.Product;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface  ProductAPI {

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
    Observable<List<Product>> getProductsByUserID(
            @Header("Authorization") String token,
            @Query("supplier") int userID
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

    @DELETE("product/{id}/")
    Observable<Product> deleteProductById(
            @Header("Authorization") String token,
            @Path("id") int productID
    );

    @FormUrlEncoded
    @PATCH("product/{id}/")
    Observable<Product> updateProductStatus(
            @Header("Authorization") String token,
            @Path("id") int id,
            @FieldMap Map<String, String> status
    );
}
