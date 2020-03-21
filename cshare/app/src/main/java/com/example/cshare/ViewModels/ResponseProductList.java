package com.example.cshare.ViewModels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cshare.Models.Product;
import com.example.cshare.WebServices.NetworkError;

import java.util.List;

import static com.example.cshare.ViewModels.Status.ERROR;
import static com.example.cshare.ViewModels.Status.LOADING;
import static com.example.cshare.ViewModels.Status.SUCCESS;

public class ResponseProductList {

    public final Status status;

    @Nullable
    public final List<Product> data;

    @Nullable
    public final NetworkError error;

    private ResponseProductList(Status status, @Nullable List<Product> data, @Nullable NetworkError error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ResponseProductList loading() {
        return new ResponseProductList(LOADING, null, null);
    }

    public static ResponseProductList success(@NonNull List<Product> data) {
        return new ResponseProductList(SUCCESS, data, null);
    }

    public static ResponseProductList error(@NonNull NetworkError error) {
        return new ResponseProductList(ERROR, null, error);
    }
}
