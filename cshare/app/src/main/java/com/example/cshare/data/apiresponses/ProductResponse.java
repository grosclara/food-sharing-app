package com.example.cshare.data.apiresponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.data.models.Product;

import static com.example.cshare.data.apiresponses.Status.COMPLETE;
import static com.example.cshare.data.apiresponses.Status.ERROR;
import static com.example.cshare.data.apiresponses.Status.SUCCESS;

/**
 * Class that contains the response to a query returning a product.
 * <p>
 * The class consists of a status that indicates the status of the response returned by the server.
 * If successful, the response contains the returned product. In case of failure, the response
 * contains an instance of the ApiError class to access the error message returned by the server.
 * <p>
 * The defined methods are on the one hand the getters and on the other hand methods that allow to
 * create instances of the class by associating them a certain status
 *
 * @see Status
 * @see ApiError
 * @see Product
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class ProductResponse {
    /**
     * Response status
     */
    public final Status status;
    /**
     * Response body of the request in case of success
     */
    @Nullable
    public final Product product;
    /**
     * Response error object in case of failure
     */
    @Nullable
    public final ApiError error;

    /**
     * Class constructor
     *
     * @param status
     * @param product
     * @param error
     */
    private ProductResponse(Status status, @Nullable Product product, @Nullable ApiError error) {
        this.status = status;
        this.product = product;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }
    @Nullable
    public Product getProduct() {
        return product;
    }
    @Nullable
    public ApiError getError() {
        return error;
    }

    /**
     * Following a successful response, this method returns a ProductResponse object containing the
     * corresponding status SUCCESS and the response body which is a product
     * @param product (Product)
     * @return ProductResponse
     * @see Status#SUCCESS
     * @see Product
     */
    public static ProductResponse success(@NonNull Product product) {
        return new ProductResponse(SUCCESS, product, null);
    }

    /**
     * Following a failure when querying the server response, this method returns a ProductResponse
     * object containing the corresponding status ERROR and a ApiError object that contains the
     * error message from the server.
     * @param error (ApiError) Error message from the server
     * @return ProductResponse
     * @see Status#ERROR
     * @see ApiError
     */
    public static ProductResponse error(@NonNull ApiError error) {
        return new ProductResponse(ERROR, null, error);
    }

    /**
     * Special method to reset the response once the request has taken place and the result has
     * been processed.
     * @return ProductResponse
     * @see Status#COMPLETE
     */
    public static ProductResponse complete(){
        return new ProductResponse(COMPLETE, null, null);
    }
}
