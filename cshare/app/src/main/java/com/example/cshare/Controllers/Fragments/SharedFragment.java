package com.example.cshare.Controllers.Fragments;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.Models.Product;
import com.example.cshare.ViewModels.ProductViewModel;

import java.util.List;

public class SharedFragment extends ProductListFragment {

    private ProductViewModel productViewModel;

    @Override
    protected BaseFragment newInstance() {
        return new SharedFragment();
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        // Set data
        productViewModel.getSharedProductList().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                adapter.updateProducts(products);
            }
        });

    }

    @Override
    protected void configureSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productViewModel.update();
                // Stop refreshing and clear actual list of users
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
