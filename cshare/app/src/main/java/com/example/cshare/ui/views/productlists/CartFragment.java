package com.example.cshare.ui.views.productlists;


import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.data.models.Product;
import com.example.cshare.ui.viewmodels.CartViewModel;
import com.example.cshare.ui.viewmodels.ProfileViewModel;
import com.example.cshare.utils.Constants;

public class CartFragment extends ProductListFragment {

    private ProfileViewModel profileViewModel;
    private CartViewModel cartViewModel;

    private static String tag;


    @Override
    protected void configureViewModel() {
        // Retrieve view models
        profileViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ProfileViewModel.class);
        cartViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(CartViewModel.class);
    }

    @Override
    protected void observeDataChanges() {
        this.getProducts();
    }

    @Override
    protected void getProducts(){
        cartViewModel.getProductPagedList().observe(this, new Observer<PagedList<Product>>() {
            @Override
            public void onChanged(PagedList<Product> products) {
                adapter.submitList(products);
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
    protected void click(Product product) {
        if (product.getStatus().equals(Constants.COLLECTED)) {
            tag = Constants.INCART;
        } else {
            tag = Constants.ARCHIVED;
        }
        DialogFragment productDetailsFragment = new ProductDialogFragment(product, tag, profileViewModel);
        productDetailsFragment.show(getChildFragmentManager(), tag);
    }
}
