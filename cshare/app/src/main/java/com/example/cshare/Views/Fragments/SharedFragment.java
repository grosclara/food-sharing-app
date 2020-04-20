package com.example.cshare.Views.Fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.Models.Product;
import com.example.cshare.ViewModels.ProductViewModel;
import com.example.cshare.ViewModels.ProfileViewModel;

import java.util.List;

public class SharedFragment extends ProductListFragment {

    private ProductViewModel productViewModel;
    private ProfileViewModel profileViewModel;

    private static final String tag = "shared";


    @Override
    protected BaseFragment newInstance() {
        return new SharedFragment();
    }

    @Override
    protected void click(Product product) {
        DialogFragment productDetailsFragment = new ProductDialogFragment(getContext(), product, tag, productViewModel, profileViewModel);
        productDetailsFragment.show(getChildFragmentManager(), tag);
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
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
