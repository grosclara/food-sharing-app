package com.example.frontend;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that represents the API of the web service in our app
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public interface DjangoRestApi {

    /** Base Url of the API */
    String baseUrl = "http://10.0.2.2:8000/api/v1/";

    /**
     * @return a call object containing a list of products
     */
    @GET("product/")
    Call<List<Product>> getAllProducts();

    /**
     * @param productId
     * @return a call object containing a single product chosen by its id
     */
    @GET("product/{id}/")
    Call<Product> getProduct(@Path("id") int productId);

    /**
     * @param product
     * @return a call object containing the product to post to the api
     */
    @POST("product/")
    Call<Product> createProduct(@Body Product product );

}
