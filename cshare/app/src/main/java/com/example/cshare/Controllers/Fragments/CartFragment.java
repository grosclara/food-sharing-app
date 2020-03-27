package com.example.cshare.Controllers.Fragments;


import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.cshare.Models.Product;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.CartViewModel;

import java.util.List;

public class CartFragment extends ProductListFragment {

    private CartViewModel cartViewModel;

    @Override
    protected void configureSwipeRefreshLayout() {

    }

    @Override
    protected BaseFragment newInstance() {
        return new CartFragment();
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        // Set data
        cartViewModel.getCartMutableLiveData(Constants.TOKEN, Constants.USERID).observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                adapter.updateProducts(products);
            }
        });

    }

}
