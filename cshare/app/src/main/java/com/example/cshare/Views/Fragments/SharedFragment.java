package com.example.cshare.Views.Fragments;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.Models.ApiResponses.ProductResponse;
import com.example.cshare.Models.ApiResponses.ProductListResponse;
import com.example.cshare.Models.Product;
import com.example.cshare.Models.User;
import com.example.cshare.RequestManager.Status;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.HomeViewModel;
import com.example.cshare.ViewModels.ProductViewModel;
import com.example.cshare.ViewModels.ProfileViewModel;
import com.example.cshare.ViewModels.SharedViewModel;

public class SharedFragment extends ProductListFragment {

    private ProductViewModel productViewModel;
    private ProfileViewModel profileViewModel;
    private SharedViewModel sharedViewModel;
    private HomeViewModel homeViewModel;

    @Override
    protected BaseFragment newInstance() {
        return new SharedFragment();
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        productViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ProductViewModel.class);
        profileViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ProfileViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(SharedViewModel.class);
        homeViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(HomeViewModel.class);

        sharedViewModel.getProductPagedList().observe(this, new Observer<PagedList<Product>>() {
            @Override
            public void onChanged(PagedList<Product> products) {
                Log.d(Constants.TAG, "OBSERVE");
                adapter.submitList(products);
            }
        });
        productViewModel.getDeleteProductResponse().observe(getViewLifecycleOwner(), new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {

                if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getContext(), "Product successfully deleted", Toast.LENGTH_SHORT).show();
                    sharedViewModel.refresh();
                    homeViewModel.refresh();

                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                sharedViewModel.refresh();
                // Stop refreshing and clear actual list of users
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    protected void configureProgressBar() { }

    @Override
    protected void click(Product product) {
        if (isClickable) {

            DialogFragment productDetailsFragment = new ProductDialogFragment(product, Constants.SHARED, profileViewModel);
            productDetailsFragment.show(getChildFragmentManager(), Constants.SHARED);
        }
    }

}
