package com.example.cshare.Views.Fragments;


import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.Models.ApiResponses.ProductResponse;
import com.example.cshare.RequestManager.Status;
import com.example.cshare.ViewModels.CartViewModel;
import com.example.cshare.ViewModels.HomeViewModel;
import com.example.cshare.ViewModels.ProfileViewModel;
import com.example.cshare.Models.Product;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.ProductViewModel;
import com.example.cshare.ViewModels.SharedViewModel;


public class HomeFragment extends ProductListFragment {

    private ProductViewModel productViewModel;
    private ProfileViewModel profileViewModel;
    private HomeViewModel homeViewModel;
    private SharedViewModel sharedViewModel;
    private CartViewModel cartViewModel;

    private static String tag;

    @Override
    protected BaseFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected void configureViewModel() {
        // Retrieve view models
        productViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ProductViewModel.class);
        profileViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ProfileViewModel.class);
        homeViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(HomeViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(SharedViewModel.class);
        cartViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(CartViewModel.class);

        homeViewModel.getProductPagedList().observe(this, new Observer<PagedList<Product>>() {
            @Override
            public void onChanged(PagedList<Product> products) {
                adapter.submitList(products);
            }
        });
        productViewModel.getDeleteProductResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {

                if (response.getStatus().equals(Status.SUCCESS)) {

                    Toast.makeText(getContext(), "Product successfully deleted", Toast.LENGTH_SHORT).show();
                    homeViewModel.refresh();
                    sharedViewModel.refresh();

                    productViewModel.getDeleteProductResponse().setValue(ProductResponse.complete());

                } else if (response.getStatus().equals(Status.ERROR)) {

                    if (response.getError().getDetail() != null){
                        Toast.makeText(getContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }
                    productViewModel.getDeleteProductResponse().setValue(ProductResponse.complete());
                }
            }
        });
        productViewModel.getOrderProductResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {

                if (response.getStatus().equals(Status.SUCCESS)) {

                    Toast.makeText(getContext(), "Product successfully ordered", Toast.LENGTH_SHORT).show();
                    homeViewModel.refresh();
                    cartViewModel.refresh();

                    productViewModel.getOrderProductResponse().setValue(ProductResponse.complete());

                } else if (response.getStatus().equals(Status.ERROR)) {

                    if (response.getError().getDetail() != null){
                        Toast.makeText(getContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }
                    productViewModel.getOrderProductResponse().setValue(ProductResponse.complete());
                }
            }
        });

    }

    /** Configure the SwipeRefreshLayout
     * We create a method that will allow us to configure our SwipeRefreshLayout and especially
     * to add a listener to it. The latter will be launched when the user performs a
     * Pull To Refresh" and triggers the onRefresh() method which will launch our usual stream.
     **/
    @Override
    public void configureSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeViewModel.refresh();
                // Stop refreshing and clear actual list of users
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void configureProgressBar() {}

    @Override
    protected void click(Product product) {
        // Check whether the current user is the supplier of the product or not
        // (if yes, he won't be able to order it)
        if (isClickable) {
            if (product.getSupplier() == profileViewModel.getUserProfileMutableLiveData().getValue().getUser().getId()) {
                tag = Constants.SHARED;
            } else {
                tag = Constants.ORDER;
            }
            DialogFragment productDetailsFragment = new ProductDialogFragment(product, tag, profileViewModel);
            productDetailsFragment.show(getChildFragmentManager(), tag);

        }
    }
}