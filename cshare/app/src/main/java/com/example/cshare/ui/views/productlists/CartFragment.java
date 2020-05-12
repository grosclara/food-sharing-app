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
import com.example.cshare.ui.viewmodels.CartViewModel;
import com.example.cshare.ui.viewmodels.HomeViewModel;
import com.example.cshare.ui.viewmodels.ProductViewModel;
import com.example.cshare.ui.viewmodels.ProfileViewModel;
import com.example.cshare.ui.views.BaseFragment;

public class CartFragment extends ProductListFragment {

    private ProductViewModel productViewModel;
    private ProfileViewModel profileViewModel;
    private CartViewModel cartViewModel;
    private HomeViewModel homeViewModel;

    private static String tag;


    @Override
    protected BaseFragment newInstance() {
        return new CartFragment();
    }

    @Override
    protected void configureViewModel() {
        // Retrieve view models
        productViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ProductViewModel.class);
        profileViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ProfileViewModel.class);
        cartViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(CartViewModel.class);
        homeViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(HomeViewModel.class);

        cartViewModel.getProductPagedList().observe(this, new Observer<PagedList<Product>>() {
            @Override
            public void onChanged(PagedList<Product> products) { adapter.submitList(products); }
        });
        productViewModel.getCancelOrderResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {

                if (response.getStatus().equals(Status.SUCCESS)) {

                    Toast.makeText(getContext(), "Order successfully canceled", Toast.LENGTH_SHORT).show();
                    homeViewModel.refresh();
                    cartViewModel.refresh();

                    productViewModel.getCancelOrderResponse().setValue(ProductResponse.complete());

                } else if (response.getStatus().equals(Status.ERROR)) {

                    productViewModel.getCancelOrderResponse().setValue(ProductResponse.complete());

                    if (response.getError().getDetail() != null){
                        Toast.makeText(getContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        productViewModel.getDeliverProductResponse().observe(this, new Observer<ProductResponse>() {
            @Override
            public void onChanged(ProductResponse response) {

                if (response.getStatus().equals(Status.SUCCESS)) {

                    Toast.makeText(getContext(), "Product successfully delivered", Toast.LENGTH_SHORT).show();
                    cartViewModel.refresh();

                    productViewModel.getDeliverProductResponse().setValue(ProductResponse.complete());

                } else if (response.getStatus().equals(Status.ERROR)) {

                    if (response.getError().getDetail() != null){
                        Toast.makeText(getContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                    }
                    productViewModel.getDeliverProductResponse().setValue(ProductResponse.complete());

                }
            }
        });
    }

    @Override
    protected void configureSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cartViewModel.refresh();
                // Stop refreshing and clear actual list of users
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void configureProgressBar() {}

    @Override
    protected void click(Product product) {
        if (isClickable) {
            if (product.getStatus().equals(Constants.COLLECTED)) {
                tag = Constants.INCART;
            } else {
                tag = Constants.ARCHIVED;
            }
            DialogFragment productDetailsFragment = new ProductDialogFragment(product, tag, profileViewModel);
            productDetailsFragment.show(getChildFragmentManager(), tag);
        }
    }

}
