package com.example.cshare.Models.ApiResponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.Status;

import java.util.List;

import static com.example.cshare.RequestManager.Status.COMPLETE;
import static com.example.cshare.RequestManager.Status.ERROR;
import static com.example.cshare.RequestManager.Status.LOADING;
import static com.example.cshare.RequestManager.Status.SUCCESS;

public class ProductListResponse {

    public final Status status;

    @Nullable
    public final List<Product> productList;

    @Nullable
    public final Throwable error;

    private ProductListResponse(Status status, @Nullable List<Product> productList, @Nullable Throwable error) {
        this.status = status;
        this.productList = productList;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    @Nullable
    public List<Product> getProductList() {
        return productList;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    public static ProductListResponse loading() {
        return new ProductListResponse(LOADING, null, null);
    }

    public static ProductListResponse success(@NonNull List<Product> productList) {
        return new ProductListResponse(SUCCESS, productList, null);
    }

    public static ProductListResponse error(@NonNull Throwable error) {
        return new ProductListResponse(ERROR, null, error);
    }

    public static ProductListResponse complete(){
        return new ProductListResponse(COMPLETE, null, null);
    }
}
