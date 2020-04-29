package com.example.cshare.Views.Fragments;


import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.Models.ApiResponses.ProductResponse;
import com.example.cshare.RequestManager.Status;
import com.example.cshare.ViewModels.HomeViewModel;
import com.example.cshare.ViewModels.ProfileViewModel;
import com.example.cshare.Models.Product;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.ProductViewModel;


public class HomeFragment extends ProductListFragment {

    private ProductViewModel productViewModel;
    private ProfileViewModel profileViewModel;
    private HomeViewModel homeViewModel;
    private static String tag;

    @Override
    protected BaseFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        productViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ProductViewModel.class);
        profileViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ProfileViewModel.class);

        homeViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(HomeViewModel.class);
        homeViewModel.getProductPagedList().observe(this, new Observer<PagedList<Product>>() {
            @Override
            public void onChanged(PagedList<Product> products) {
                adapter.submitList(products);
            }
        });



        /*// Set data
        productViewModel.getAvailableProductList().observe(getViewLifecycleOwner(), new Observer<ProductListResponse>() {
            @Override
            public void onChanged(@Nullable ProductListResponse response) {
                Log.d(Constants.TAG, "ONCHANGED");
                Log.d(Constants.TAG, response.getStatus().toString());
                if (response.getStatus().equals(Status.SUCCESS)) {
                    //adapter.setProducts(response.getProductList());
                    Toast.makeText(getContext(), String.valueOf(response.getApiProductListResponse().getProductList().size()), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    isClickable = true;
                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    isClickable = false;
                    progressBar.setVisibility(View.GONE);
                } else if (response.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.VISIBLE);
                    isClickable = false;
                }
            }
        });*/

        productViewModel.getDeleteProductResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getContext(), "Product successfully deleted", Toast.LENGTH_SHORT).show();
                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    productViewModel.getDeleteProductResponse().setValue(ProductResponse.complete());
                } else if (response.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                }
            }
        });

        productViewModel.getOrderProductResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getContext(), "Product successfully ordered", Toast.LENGTH_SHORT).show();
                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    productViewModel.getOrderProductResponse().setValue(ProductResponse.complete());
                } else if (response.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*profileViewModel.getOtherProfileMutableLiveData().observe(this, new Observer<UserReponse>() {
            @Override
            public void onChanged(UserReponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getContext(), "Supplier retrieved", Toast.LENGTH_SHORT).show();
                    Log.d(Constants.TAG, String.valueOf(profileViewModel.getOtherProfileMutableLiveData().getValue().getStatus()));


                    User supplier = profileViewModel.getOtherProfileMutableLiveData().getValue().getUser();
                    Log.d(Constants.TAG, supplier.getFirstName());



                } else if (response.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getContext(), response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    productViewModel.getOrderProductResponse().setValue(ProductResponse.complete());
                } else if (response.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getContext(), "Loading", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }

    // Configure the SwipeRefreshLayout
    // We create a method that will allow us to configure our SwipeRefreshLayout and especially
    // to add a listener to it. The latter will be launched when the user performs a
    // "Pull To Refresh" and triggers the onRefresh() method which will launch our usual stream.
    public void configureSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productViewModel.updateAvailableProducts();
                // Stop refreshing and clear actual list of users
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void configureProgressBar() {
    }

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