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

/**
 * Interface that defines the product-related API endpoints.
 * <p>
 * Provides endpoints to retrieve products, to add a new product, or to delete an existing product.
 *
 * @since 1.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public interface ProductAPI {

    /**
     * Returns a Call object that contains the response to the API's getProducts request.
     * 
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds
     *              to the token of the authenticated user and it is passed in the request header
     * @param status (String) Product status (query parameter in the url)
     * @param category (String) Product category (query parameter in the url)
     * @param shared (int) if true (1), retrieve shared products (query parameter in the url)
     * @param page (int) number of the page to display (query parameter in the url)
     * @return (Call) A Call object containing the product list and pagination information in a
     * {@link ProductListResponse.ApiProductListResponse} object
     * @see ProductListResponse.ApiProductListResponse
     * @see com.example.cshare.data.sources.HomeDataSource
     */
    @GET("product/")
    Call<ProductListResponse.ApiProductListResponse> getProducts(
            @Header("Authorization") String token,
            @Query("status") String status,
            @Query("category") String category,
            @Query("shared") int shared,
            @Query("page") int page
    );

    /**
     * Returns a Call object that contains the response to the API's addProduct request.
     * 
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds
     *              to the token of the authenticated user and it is passed in the request header
     * @param product_picture (Image) Mandatory product picture (request body)
     * @param name (String) name of the added product (request body)
     * @param category (String) product category (request body)
     * @param quantity (String) product quantity (request body)
     * @param expiration_date (String) expiration date (with format YYYY-MM-DD) (request body)
     * @return (Call) A Call object containing the added product in a {@link Product} object
     * @see Product
     * @see com.example.cshare.data.sources.ProductRequestManager#addProduct(Product) 
     */
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

    /**
     * Returns a Call object that contains the response to the API's deleteProduct request.
     * 
     * @param token (String) of the form "token eyJ0eXAiOiAianFsZyI6ICJIUzUxMiJ9", it corresponds 
     *              to the token of the authenticated user and it is passed in the request header
     * @param productID (int) corresponds to the id of the product to delete in the url
     * @return (Call) A Call object containing the deleted product in a {@link Product} object
     * @see Product
     * @see com.example.cshare.data.sources.ProductRequestManager#deleteProduct(int) 
     */
    @DELETE("product/{id}/")
    Call<Product> deleteProduct(
            @Header("Authorization") String token,
            @Path("id") int productID
    );
}
