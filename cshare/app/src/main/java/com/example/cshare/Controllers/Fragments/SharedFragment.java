package com.example.cshare.Controllers.Fragments;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cshare.Models.Product;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.SharedProductsViewModel;

import java.util.List;

public class SharedFragment extends ProductListFragment {

    private SharedProductsViewModel sharedProductsViewModel;

    @Override
    protected BaseFragment newInstance() {
        return new SharedFragment();
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        sharedProductsViewModel = new ViewModelProvider(this).get(SharedProductsViewModel.class);
       // Set data
        sharedProductsViewModel.getSharedProductsMutableLiveData(Constants.TOKEN, Constants.USERID).observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                adapter.updateProducts(products);
            }
        });

    }

    @Override
    protected void configureViewModel() {

    }
}
