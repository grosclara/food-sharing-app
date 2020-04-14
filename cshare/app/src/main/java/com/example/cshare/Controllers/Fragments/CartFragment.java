package com.example.cshare.Controllers.Fragments;


import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.Models.Product;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.ProductViewModel;

import java.util.List;

public class CartFragment extends ProductListFragment {

    private ProductViewModel productViewModel;

    private static String tag;

    @Override
    protected BaseFragment newInstance() {
        return new CartFragment();
    }

    @Override
    protected void click(Product product) {
        if (product.getStatus().equals(Constants.COLLECTED)){
            tag = Constants.INCART;} else {tag = Constants.ARCHIVED;}
        DialogFragment productDetailsFragment = new ProductDialogFragment(getContext(), product, tag, productViewModel);
        productDetailsFragment.show(getChildFragmentManager(), tag);
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        // Set data
        productViewModel.getInCartProductList().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
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
