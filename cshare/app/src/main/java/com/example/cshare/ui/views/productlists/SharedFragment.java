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

/**
 * Fragment inheriting from the ProductListFragment class that displays the list of products
 * the user has shared on the platform.
 *
 * @see ProductListFragment
 * @see ProfileViewModel
 * @see SharedViewModel
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class SharedFragment extends ProductListFragment {

    private ProfileViewModel profileViewModel;
    private SharedViewModel sharedViewModel;

    @Override
    protected void configureViewModel() {
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

    /**
     * Method called when clicking on an item of the recyclerView to interact with products.
     * <p>
     * Display an appropriate ProductDialogFragment to see and optionally to delete the shared
     * product through the string Constant.SHARED passed as parameter when creating the
     * productDialogFragment object.
     *
     * @param product (Product) the item clicked on
     * @see ProductDialogFragment
     */
    @Override
    protected void click(Product product) {
            DialogFragment productDetailsFragment = new ProductDialogFragment(getActivity(), product,
                    Constants.SHARED
            );
            productDetailsFragment.show(getChildFragmentManager(), Constants.SHARED);
    }
}
