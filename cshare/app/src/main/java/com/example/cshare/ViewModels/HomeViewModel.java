package com.example.cshare.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.HomeRequestManager;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Product>> homeMutableLiveData;

    public HomeViewModel() {
        // Retrieve available products list from request manager
        homeMutableLiveData = HomeRequestManager.getInstance().getProductList();
    }

    // Getter method
    public MutableLiveData<List<Product>> getHomeMutableLiveData() {
        return homeMutableLiveData;
    }
}
