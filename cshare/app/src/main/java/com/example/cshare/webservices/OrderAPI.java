package com.example.cshare.webservices;

import com.example.cshare.data.apiresponses.ProductListResponse;
import com.example.cshare.data.models.Product;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    /**
     * Returns a Call object that contains the response to the API's getOrderedProducts request.
     *
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds
     *              to the token of the authenticated user and it is passed in the request header
     * @param page (int) number of the page to display (query parameter in the url)
     * @return (Call) A Call object containing a product list and pagination information in a
     * {@link ProductListResponse} object
     * @see ProductListResponse
     * @see com.example.cshare.data.sources.CartDataSource
     */
    @GET("order/")
    Call<ProductListResponse> getOrderedProducts(
            @Header("Authorization") String token,
            @Query("page") int page
    );

    /**
     * Returns a Call object that contains the response to the API's order request.
     * <p>
     * Performing form-urlencoded requests using Retrofit (through the annotation @FormUrlEncoded)
     * will adjust the proper mime type of your request automatically to
     * application/x-www-form-urlencoded
     *
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds
     *              to the token of the authenticated user and it is passed in the request header
     * @param productID (int) ID of the product to order (request body)
     * @return (Call) A Call object containing the ordered product in a {@link Product} object
     * @see Product
     * @see com.example.cshare.data.sources.ProductRequestManager#order(int)
     */
    @POST("order/")
    Call<Product> order(
            @Header("Authorization") String token,
            @Body Map<String, Integer> productID
    );

    /**
     * Returns a Call object that contains the response to the API's cancelOrder request.
     *
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds
     *              to the token of the authenticated user and it is passed in the request header
     * @param productID (int) ID of the product to order passed in the url
     * @return (Call) A Call object containing the product of the deleted order
     * in a {@link Product} object
     * @see Product
     * @see com.example.cshare.data.sources.ProductRequestManager#deleteProduct(int)
     */
    @DELETE("order/{id}/")
    Call<Product> cancelOrder(
            @Header("Authorization") String token,
            @Path("id") int productID
    );

    /**
     * Returns a Call object that contains the response to the API's deliverOrder request.
     *
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds
     *              to the token of the authenticated user and it is passed in the request header
     * @param productID (int) ID of the product to order passed in the url
     * @return (Call) A Call object containing the delivered product in a {@link Product} object
     * @see Product
     * @see com.example.cshare.data.sources.ProductRequestManager#deliver(int)
     */
    @PATCH("order/{id}/")
    Call<Product> deliverOrder(
            @Header("Authorization") String token,
            @Path("id") int productID
    );
}
