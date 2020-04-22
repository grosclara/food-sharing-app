package com.example.cshare.Views.Fragments;


import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.Models.Response.ApiEmptyResponse;
import com.example.cshare.Models.Response.ResponseProductList;
import com.example.cshare.RequestManager.Status;
import com.example.cshare.ViewModels.ProfileViewModel;
import com.example.cshare.Models.Product;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.ProductViewModel;


public class HomeFragment extends ProductListFragment {

    private ProductViewModel productViewModel;
    private ProfileViewModel profileViewModel;
    private static String tag;

    @Override
    protected BaseFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Set data
        productViewModel.getAvailableProductList().observe(getViewLifecycleOwner(), new Observer<ResponseProductList>() {
            @Override
            public void onChanged(@Nullable ResponseProductList response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    adapter.updateProducts(response.getProductList());
                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else if (response.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                }
            }
        });
        productViewModel.getDeleteProductResponse().observe(this, new Observer<ApiEmptyResponse>() {
            @Override
            public void onChanged(ApiEmptyResponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getContext(), "Product successfully deleted", Toast.LENGTH_SHORT).show();
                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    productViewModel.getDeleteProductResponse().setValue(ApiEmptyResponse.complete());
                } else if (response.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                }
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
                productViewModel.update();
                // Stop refreshing and clear actual list of users
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void click(Product product) {
        // Check whether the current user is the supplier of the product or not
        // (if yes, he won't be able to order it)

        if (product.getSupplier() == profileViewModel.getUserID()) {
            tag = Constants.SHARED;
        } else {
            tag = Constants.ORDER;
        }
        DialogFragment productDetailsFragment = new ProductDialogFragment(getContext(), product, tag, productViewModel, profileViewModel);
        productDetailsFragment.show(getChildFragmentManager(), tag);
    }

}