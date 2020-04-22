package com.example.cshare.Views.Fragments;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.Models.Response.ApiEmptyResponse;
import com.example.cshare.Models.Response.ResponseProductList;
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
        DialogFragment productDetailsFragment = new ProductDialogFragment(getContext(), product, tag, productViewModel, profileViewModel);
        productDetailsFragment.show(getChildFragmentManager(), tag);
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // Set data
        productViewModel.getSharedProductList().observe(getViewLifecycleOwner(), new Observer<ResponseProductList>() {
            @Override
            public void onChanged(@Nullable ResponseProductList response) {
                if (response.getStatus().equals(Status.SUCCESS)){
                    adapter.updateProducts(response.getProductList());
                }
                else if (response.getStatus().equals(Status.ERROR)){
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                else if (response.getStatus().equals(Status.LOADING)){
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                }
            }
        });
        productViewModel.getDeleteProductResponse().observe(getViewLifecycleOwner(), new Observer<ApiEmptyResponse>() {
            @Override
            public void onChanged(ApiEmptyResponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getContext(), "Product successfully deleted", Toast.LENGTH_SHORT).show();
                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
}
