package com.example.cshare.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.HomeRequestManager;
import com.example.cshare.Utils.Constants;

import java.util.List;

import okhttp3.MultipartBody;

public class HomeViewModel extends ViewModel {
    private HomeRequestManager homeRequestManager;
    private MutableLiveData<List<Product>> homeMutableLiveData;

    public HomeViewModel() {
        // Get request manager instance
        homeRequestManager = HomeRequestManager.getInstance();
        // Retrieve available products list from request manager
        homeMutableLiveData = homeRequestManager.getProductList();

    }

    // Getter method
    public MutableLiveData<List<Product>> getHomeMutableLiveData() {
        return homeMutableLiveData;
    }

    // Insert product method
    public boolean addProduct(MultipartBody.Part product_picture, String productName, String productCategory, String quantity, String expiration_date){
        homeRequestManager.addProduct(product_picture, productName, productCategory, quantity, expiration_date);
        return true;
    }

    // Insert product in request manager
    public void insert(Product product) {
        homeRequestManager.insert(product);
    }

}
