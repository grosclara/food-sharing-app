package com.example.cshare.WebServices;

import com.example.cshare.Models.ApiResponses.ProductListResponse;
import com.example.cshare.Models.Product;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
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
    Observable<Product> addProduct(
            @Header("Authorization") String token,
            @Part MultipartBody.Part product_picture,
            @Part("name") String name,
            @Part("category") String category,
            @Part("quantity") String quantity,
            @Part("expiration_date") String expiration_date
    );

    @DELETE("product/{id}/")
    Observable<Product> deleteProduct(
            @Header("Authorization") String token,
            @Path("id") int productID
    );
}
