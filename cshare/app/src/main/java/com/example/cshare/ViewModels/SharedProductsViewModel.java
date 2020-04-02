package com.example.cshare.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.SharedProductsRequestManager;
import com.example.cshare.Utils.Constants;

import java.util.List;

public class SharedProductsViewModel extends ViewModel {

    private MutableLiveData<List<Product>> sharedProductsMutableLiveData;
    private SharedProductsRequestManager sharedProductsRequestManager;

    public SharedProductsViewModel() {
        // Retrieve collected products list from request manager
        sharedProductsRequestManager =SharedProductsRequestManager.getInstance();
        sharedProductsMutableLiveData = sharedProductsRequestManager.getProductList();
    }

    // Getter method
    public MutableLiveData<List<Product>> getSharedProductsMutableLiveData() {
        return sharedProductsMutableLiveData;
    }

    public SharedProductsRequestManager getSharedProductsRequestManager() {
        return sharedProductsRequestManager;
    }

    // Insert product in request manager
    public void insert(Product product) {
        sharedProductsRequestManager.insert(product);
    }

    // Update products in request manager
    public void update() {sharedProductsRequestManager.updateRequestManager();}

}
