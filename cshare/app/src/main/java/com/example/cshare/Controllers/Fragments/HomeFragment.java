package com.example.cshare.Controllers.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cshare.Models.Product;
import com.example.cshare.R;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.HomeViewModel;

import java.util.List;


public class HomeFragment extends ProductListFragment {

    private HomeViewModel homeViewModel;

    @Override
    protected BaseFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // Set data
        homeViewModel.getHomeMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
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