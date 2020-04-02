package com.example.cshare.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.CartRequestManager;

import java.util.List;

public class CartViewModel extends ViewModel {

    private MutableLiveData<List<Product>> cartMutableLiveData;
    private CartRequestManager cartRequestManager;

    public CartViewModel() {
        // Retrieve a list of the in-cart products from the request manager
        cartRequestManager = CartRequestManager.getInstance();
        cartMutableLiveData = cartRequestManager.getProductList();
    }

    public CartRequestManager getCartRequestManager() {
        return cartRequestManager;
    }


    // Getter method
    public MutableLiveData<List<Product>> getCartMutableLiveData(String token, int customerID) {
        return cartMutableLiveData;
    }
}
