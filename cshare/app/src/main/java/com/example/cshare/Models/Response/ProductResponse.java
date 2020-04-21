package com.example.cshare.Models.Response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.Status;

import static com.example.cshare.RequestManager.Status.COMPLETE;
import static com.example.cshare.RequestManager.Status.ERROR;
import static com.example.cshare.RequestManager.Status.LOADING;
import static com.example.cshare.RequestManager.Status.SUCCESS;

public class ProductResponse {

    public final Status status;

    @Nullable
    public final Product product;

    @Nullable
    public final Throwable error;

    private ProductResponse(Status status, @Nullable Product product, @Nullable Throwable error) {
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
    public Throwable getError() {
        return error;
    }

    public static ProductResponse loading() {
        return new ProductResponse(LOADING, null, null);
    }

    public static ProductResponse success(@NonNull Product product) {
        return new ProductResponse(SUCCESS, product, null);
    }

    public static ProductResponse error(@NonNull Throwable error) {
        return new ProductResponse(ERROR, null, error);
    }

    public static ProductResponse complete(){
        return new ProductResponse(COMPLETE, null, null);
    }
}
