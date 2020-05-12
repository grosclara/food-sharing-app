package com.example.cshare.ui.views.productlists;

import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.data.apiresponses.ProductResponse;
import com.example.cshare.data.models.Product;
import com.example.cshare.data.apiresponses.Status;
import com.example.cshare.utils.Constants;
import com.example.cshare.ui.viewmodels.HomeViewModel;
import com.example.cshare.ui.viewmodels.ProductViewModel;
import com.example.cshare.ui.viewmodels.ProfileViewModel;
import com.example.cshare.ui.viewmodels.SharedViewModel;
import com.example.cshare.ui.views.BaseFragment;

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

                    productViewModel.getDeleteProductResponse().setValue(ProductResponse.complete());

                } else if (response.getStatus().equals(Status.ERROR)) {

                    if (response.getError().getDetail() != null){
                        Toast.makeText(getContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }
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
