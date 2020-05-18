package com.example.cshare.ui.views.productlists;


import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.data.apiresponses.ProductResponse;
import com.example.cshare.data.apiresponses.Status;
import com.example.cshare.data.models.Product;
import com.example.cshare.ui.viewmodels.CartViewModel;
import com.example.cshare.ui.viewmodels.HomeViewModel;
import com.example.cshare.ui.viewmodels.ProductViewModel;
import com.example.cshare.ui.viewmodels.ProfileViewModel;
import com.example.cshare.ui.viewmodels.SharedViewModel;
import com.example.cshare.utils.Constants;

/**
 * Fragment inheriting from the ProductListFragment class that displays the list of products
 * available on the user's campus.
 * <p>
 *
 *
 * @see ProductListFragment
 * @see ProductViewModel
 * @see ProfileViewModel
 * @see HomeViewModel
 * @see SharedViewModel
 * @see CartViewModel
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class HomeFragment extends ProductListFragment {

    private ProfileViewModel profileViewModel;
    private HomeViewModel homeViewModel;

    private static String tag;

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
     * Check whether the current user is the supplier of the product or not and then display an
     * appropriate ProductDialogFragment
     *
     * @param product (Product) the item clicked on
     * @see ProductDialogFragment
     */
    @Override
    protected void click(Product product) {
        // Check whether the current user is the supplier of the product or not
        if (product.getSupplier() == profileViewModel.getUserProfileMutableLiveData()
                .getValue().getUser().getId()) {
            tag = Constants.SHARED;
        } else {
            tag = Constants.ORDER;
        }
        DialogFragment productDetailsFragment = new ProductDialogFragment(product,
                tag,
                profileViewModel);
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