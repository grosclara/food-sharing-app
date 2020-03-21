package com.example.cshare.ViewModels;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.CartRequestManager;
import com.example.cshare.RequestManager.SharedProductsRequestManager;

import java.util.List;

public class SharedProductsViewModel {

    private MutableLiveData<List<Product>> sharedProductsMutableLiveData;

    public SharedProductsViewModel() {
        /**
         * Constructor that receives application as argument
         */
        // Retrieve collected products list from repository
        sharedProductsMutableLiveData = SharedProductsRequestManager.getInstance().getProducts();
    }

    // Getter method
    public MutableLiveData<List<Product>> getSharedProductsMutableLiveData() {
        return sharedProductsMutableLiveData;
    }
}
