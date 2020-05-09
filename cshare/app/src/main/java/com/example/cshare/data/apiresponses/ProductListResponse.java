package com.example.cshare.data.apiresponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.data.models.Product;

import java.util.List;

import static com.example.cshare.data.apiresponses.Status.COMPLETE;
import static com.example.cshare.data.apiresponses.Status.ERROR;
import static com.example.cshare.data.apiresponses.Status.LOADING;
import static com.example.cshare.data.apiresponses.Status.SUCCESS;

public class ProductListResponse {

    public class ApiProductListResponse {

        private List<Product> results;
        private int count;
        private String previous;
        private String next;

        public ApiProductListResponse(List<Product> productList, int count, String previous, String next) {
            this.results = productList;
            this.count = count;
            this.previous = previous;
            this.next = next;
        }

        public List<Product> getProductList() {
            return results;
        }

        public String getPrevious() {
            return previous;
        }

        public String getNext() {
            return next;
        }
    }

    private Status status;

    @Nullable
    private final ApiProductListResponse apiProductListResponse;

    @Nullable
    private final Throwable error;

    private ProductListResponse(Status status, @Nullable ApiProductListResponse apiProductListResponse, @Nullable Throwable error) {
        this.status = status;
        this.apiProductListResponse = apiProductListResponse;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    @Nullable
    public ApiProductListResponse getApiProductListResponse() {
        return apiProductListResponse;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    public static ProductListResponse loading() {
        return new ProductListResponse(LOADING, null, null);
    }

    public static ProductListResponse success(@NonNull ApiProductListResponse apiProductListResponse) {
        return new ProductListResponse(SUCCESS, apiProductListResponse, null);
    }

    public static ProductListResponse error(@NonNull Throwable error) {
        return new ProductListResponse(ERROR, null, error);
    }

    public static ProductListResponse complete(){
        return new ProductListResponse(COMPLETE, null, null);
    }
}
