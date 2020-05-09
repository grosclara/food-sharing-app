package com.example.cshare.data.apiresponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.data.models.Product;

import static com.example.cshare.data.apiresponses.Status.COMPLETE;
import static com.example.cshare.data.apiresponses.Status.ERROR;
import static com.example.cshare.data.apiresponses.Status.LOADING;
import static com.example.cshare.data.apiresponses.Status.SUCCESS;

public class ProductResponse {

    public static class ProductError{
        private String detail;

        public String getDetail() {
            return detail;
        }
    }

    public final Status status;

    @Nullable
    public final Product product;

    @Nullable
    public final ProductError error;

    private ProductResponse(Status status, @Nullable Product product, @Nullable ProductError error) {
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
    public ProductError getError() {
        return error;
    }

    public static ProductResponse loading() {
        return new ProductResponse(LOADING, null, null);
    }

    public static ProductResponse success(@NonNull Product product) {
        return new ProductResponse(SUCCESS, product, null);
    }

    public static ProductResponse error(@NonNull ProductError error) {
        return new ProductResponse(ERROR, null, error);
    }

    public static ProductResponse complete(){
        return new ProductResponse(COMPLETE, null, null);
    }
}
