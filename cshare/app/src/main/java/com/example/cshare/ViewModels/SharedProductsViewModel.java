package com.example.cshare.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.SharedProductsRequestManager;

import java.util.List;

public class SharedProductsViewModel extends ViewModel {

    private MutableLiveData<List<Product>> sharedProductsMutableLiveData;

    public SharedProductsViewModel() {
        // Retrieve collected products list from request manager
        sharedProductsMutableLiveData = SharedProductsRequestManager.getInstance().getProductList();
    }

    // Getter method
    public MutableLiveData<List<Product>> getSharedProductsMutableLiveData(String token, int userID) {
        return sharedProductsMutableLiveData;
    }
}
