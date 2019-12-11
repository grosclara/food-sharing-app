package com.example.frontend;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

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
    @GET("product")
    Call<List<Product>> getAllProducts();

}
