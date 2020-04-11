package com.example.cshare.Controllers.Fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.Models.Product;

import java.util.List;

public class SharedFragment extends ProductListFragment {

    private SharedProductsViewModel sharedProductsViewModel;

    private static final String tag = "shared";


    @Override
    protected BaseFragment newInstance() {
        return new SharedFragment();
    }

    @Override
    protected void click(Product product) {
        DialogFragment productDetailsFragment = new ProductDialogFragment(getContext(), product, tag, sharedProductsViewModel);
        productDetailsFragment.show(getChildFragmentManager(), tag);
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        sharedProductsViewModel = new ViewModelProvider(this).get(SharedProductsViewModel.class);
        // Set data
        sharedProductsViewModel.getSharedProductsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
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
                sharedProductsViewModel.update();
                // Stop refreshing and clear actual list of users
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
