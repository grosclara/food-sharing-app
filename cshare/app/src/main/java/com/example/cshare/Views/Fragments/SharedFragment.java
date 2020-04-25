package com.example.cshare.Views.Fragments;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.Models.ApiResponses.ProductResponse;
import com.example.cshare.Models.ApiResponses.ProductListResponse;
import com.example.cshare.Models.Product;
import com.example.cshare.RequestManager.Status;
import com.example.cshare.ViewModels.ProductViewModel;
import com.example.cshare.ViewModels.ProfileViewModel;

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
        if (isClickable) {
            DialogFragment productDetailsFragment = new ProductDialogFragment(getContext(), product, tag, productViewModel, profileViewModel);
            productDetailsFragment.show(getChildFragmentManager(), tag);
        }
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // Set data
        productViewModel.getSharedProductList().observe(getViewLifecycleOwner(), new Observer<ProductListResponse>() {
            @Override
            public void onChanged(@Nullable ProductListResponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    adapter.updateProducts(response.getProductList());
                    progressBar.setVisibility(View.GONE);
                    isClickable = true;
                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    isClickable = false;
                } else if (response.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.VISIBLE);
                    isClickable = false;
                }
            }
        });
        productViewModel.getDeleteProductResponse().observe(getViewLifecycleOwner(), new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getContext(), "Product successfully deleted", Toast.LENGTH_SHORT).show();
                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    // Reset the status
                    productViewModel.getDeleteProductResponse().setValue(ProductResponse.complete());
                } else if (response.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    protected void configureProgressBar() {
    }
}