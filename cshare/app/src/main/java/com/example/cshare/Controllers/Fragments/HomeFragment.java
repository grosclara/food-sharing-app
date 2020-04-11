package com.example.cshare.Controllers.Fragments;


import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.Models.Product;

import java.util.List;


public class HomeFragment extends ProductListFragment {

    private HomeViewModel homeViewModel;
    private static final String tag = "order";

    @Override
    protected BaseFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected void click(Product product) {
        DialogFragment productDetailsFragment = new ProductDialogFragment(getContext(), product, tag);
        productDetailsFragment.show(getChildFragmentManager(), tag);
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

    // Configure the SwipeRefreshLayout
    // We create a method that will allow us to configure our SwipeRefreshLayout and especially
    // to add a listener to it. The latter will be launched when the user performs a
    // "Pull To Refresh" and triggers the onRefresh() method which will launch our usual stream.
    public void configureSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeViewModel.update();
                // Stop refreshing and clear actual list of users
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }

}