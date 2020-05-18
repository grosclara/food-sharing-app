package com.example.cshare.ui.views.productlists;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.data.models.Product;
import com.example.cshare.ui.viewmodels.ProfileViewModel;
import com.example.cshare.ui.viewmodels.SharedViewModel;
import com.example.cshare.utils.Constants;

public class SharedFragment extends ProductListFragment {

    private ProfileViewModel profileViewModel;
    private SharedViewModel sharedViewModel;

    @Override
    protected void configureViewModel() {
        // Retrieve data for view model
        profileViewModel = new ViewModelProvider(getActivity(),
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
        ).get(ProfileViewModel.class);
        sharedViewModel = new ViewModelProvider(getActivity(),
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
        ).get(SharedViewModel.class);
    }

    @Override
    protected void observeDataChanges() {
        this.getProducts();

    }

    @Override
    protected void getProducts(){
        sharedViewModel.getProductPagedList().observe(this, new Observer<PagedList<Product>>() {
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
                sharedViewModel.refresh();
                // Stop refreshing and clear actual list of users
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    protected void click(Product product) {
            DialogFragment productDetailsFragment = new ProductDialogFragment(product,
                    Constants.SHARED,
                    profileViewModel);
            productDetailsFragment.show(getChildFragmentManager(), Constants.SHARED);
    }

}
