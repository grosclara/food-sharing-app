package com.example.cshare.ui.views.productlists;


import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.data.models.Product;
import com.example.cshare.ui.viewmodels.HomeViewModel;
import com.example.cshare.ui.viewmodels.ProfileViewModel;
import com.example.cshare.utils.Constants;

/**
 * Fragment inheriting from the ProductListFragment class that displays the list of products
 * available on the user's campus.
 *
 * @see ProductListFragment
 * @see ProfileViewModel
 * @see HomeViewModel
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class HomeFragment extends ProductListFragment {

    private ProfileViewModel profileViewModel;
    private HomeViewModel homeViewModel;

    @Override
    protected void configureViewModel() {
        profileViewModel = new ViewModelProvider(getActivity(),
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
        ).get(ProfileViewModel.class);
        homeViewModel = new ViewModelProvider(getActivity(),
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
        ).get(HomeViewModel.class);
    }

    @Override
    protected void observeDataChanges() {
        this.getProducts();
    }

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

    /**
     * Method called when clicking on an item of the recyclerView to interact with products.
     * <p>
     * Check whether the current user is the supplier of the product or not and then display an
     * appropriate ProductDialogFragment through a string tag passed as parameter when creating the
     * productDialogFragment object.
     * <p>
     * If the user is the supplier, it will display a productDialogFragment to see and optionally
     * delete the product. Otherwise, it will display a different productDialogFragment to see and
     * optionally order the product.
     *
     * @param product (Product) the item clicked on
     * @see ProductDialogFragment
     */
    @Override
    protected void click(Product product) {
        String tag;
        // Check whether the current user is the supplier of the product or not
        // and update the tag conveniently
        if (product.getSupplier() == profileViewModel.getUserProfileMutableLiveData()
                .getValue().getUser().getId()) {
            tag = Constants.SHARED;
        } else {
            tag = Constants.ORDER;
        }

        // Create a new DialogFragment and show it
        DialogFragment productDetailsFragment = new
                ProductDialogFragment(getActivity(), product, tag
        );
        productDetailsFragment.show(getChildFragmentManager(), tag);
    }

    @Override
    protected void getProducts(){
        homeViewModel.getProductPagedList().observe(this, new Observer<PagedList<Product>>() {
            @Override
            public void onChanged(PagedList<Product> products) {
                adapter.submitList(products);
            }
        });
    }

}