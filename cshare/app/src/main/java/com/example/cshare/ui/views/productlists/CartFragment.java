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

/**
 * Fragment inheriting from the ProductListFragment class that displays the list of products
 * the user has ordered.
 *
 * @see ProductListFragment
 * @see ProfileViewModel
 * @see CartViewModel
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class CartFragment extends ProductListFragment {

    private ProfileViewModel profileViewModel;
    private CartViewModel cartViewModel;

    @Override
    protected void configureViewModel() {
        // Retrieve view models
        profileViewModel = new ViewModelProvider(getActivity(),
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
        ).get(ProfileViewModel.class);
        cartViewModel = new ViewModelProvider(getActivity(),
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
        ).get(CartViewModel.class);
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

    /**
     * Method called when clicking on an item of the recyclerView to interact with products.
     * <p>
     * Check whether the product is marked as Collected or Delivered and then display an
     * appropriate ProductDialogFragment through a string tag passed as parameter when creating the
     * productDialogFragment object.
     * <p>
     * If the product is not delivered yet, it will display a productDialogFragment to see the
     * product and optionally cancel the order or marked it as done. Otherwise, it will display a
     * different productDialogFragment to see the product.
     *
     * @param product (Product) the item clicked on
     * @see ProductDialogFragment
     */
    @Override
    protected void click(Product product) {
        String tag;
        // Check whether the product is marked as Collected or Delivered
        // and update the tag conveniently
        if (product.getStatus().equals(Constants.COLLECTED)) {
            tag = Constants.INCART;
        } else { tag = Constants.ARCHIVED; }

        // Create a new DialogFragment and show it
        DialogFragment productDetailsFragment = new ProductDialogFragment(product, tag, profileViewModel);
        productDetailsFragment.show(getChildFragmentManager(), tag);
    }
}
