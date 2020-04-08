package com.example.cshare.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cshare.Models.Product;
import com.example.cshare.Models.ProductForm;
import com.example.cshare.RequestManager.HomeRequestManager;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private HomeRequestManager homeRequestManager;
    private MutableLiveData<List<Product>> homeMutableLiveData;

    public HomeViewModel() {

        // Get request manager instance
        homeRequestManager = HomeRequestManager.getInstance();
        // Retrieve available products list from request manager
        homeMutableLiveData = homeRequestManager.getProductList();

    }

    // Get request manager
    public HomeRequestManager getHomeRequestManager() {
        return homeRequestManager;
    }

    // Getter method
    public MutableLiveData<List<Product>> getHomeMutableLiveData() {
        return homeMutableLiveData;
    }

    // Insert product method
    public boolean addProduct(ProductForm productToPost){
        homeRequestManager.addProduct(productToPost);
        return true;
    }

    // Insert product in request manager
    public void insert(Product product) {
        homeRequestManager.insert(product);
    }

    // Update products in request manager
    public void update() {homeRequestManager.updateRequestManager();}

}
