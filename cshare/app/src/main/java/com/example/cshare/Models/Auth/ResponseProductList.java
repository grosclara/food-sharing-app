package com.example.cshare.Models.Auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.Status;
import com.example.cshare.WebServices.NetworkError;

import java.util.List;

import static com.example.cshare.RequestManager.Status.COMPLETE;
import static com.example.cshare.RequestManager.Status.ERROR;
import static com.example.cshare.RequestManager.Status.LOADING;
import static com.example.cshare.RequestManager.Status.SUCCESS;

public class ResponseProductList {

    public final Status status;

    @Nullable
    public final List<Product> productList;

    @Nullable
    public final Throwable error;

    private ResponseProductList(Status status, @Nullable List<Product> productList, @Nullable Throwable error) {
        this.status = status;
        this.productList = productList;
        this.error = error;
    }

    public static ResponseProductList loading() {
        return new ResponseProductList(LOADING, null, null);
    }

    public static ResponseProductList success(@NonNull List<Product> productList) {
        return new ResponseProductList(SUCCESS, productList, null);
    }

    public static ResponseProductList error(@NonNull Throwable error) {
        return new ResponseProductList(ERROR, null, error);
    }

    public static ResponseProductList complete(){
        return new ResponseProductList(COMPLETE, null, null);
    }
}
