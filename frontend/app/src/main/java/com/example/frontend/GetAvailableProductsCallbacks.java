package com.example.frontend;

import androidx.annotation.NonNull;
import com.example.frontend.model.Product;
import java.util.ArrayList;

public interface GetAvailableProductsCallbacks {
    void onSuccess(@NonNull ArrayList<Product> productArrayList);

    void onError(@NonNull Throwable throwable);
}
