package com.example.cshare.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cshare.Models.Product;
import com.example.cshare.Repositories.CartRepository;

import java.util.List;

public class CartViewModel extends ViewModel {

    private MutableLiveData<List<Product>> cartMutableLiveData;

    public CartViewModel() {
        /**
         * Constructor that receives application as argument
         */
        // Retrieve collected products list from repository
        cartMutableLiveData = CartRepository.getInstance().getProducts();
    }

    // Getter method
    public MutableLiveData<List<Product>> getCartMutableLiveData() {
        return cartMutableLiveData;
    }

}