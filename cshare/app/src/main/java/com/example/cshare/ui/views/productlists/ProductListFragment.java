package com.example.cshare.ui.views.productlists;


import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.R;
import com.example.cshare.data.models.Product;
import com.example.cshare.ui.views.AddActivity;
import com.example.cshare.ui.views.BaseFragment;
import com.example.cshare.ui.views.productlists.adapter.ItemClickSupport;
import com.example.cshare.ui.views.productlists.adapter.ProductPagedListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Abstract ProductListFragment class, inheriting from the BaseFragment class, within which
 * the global logic for recycler view is implemented
 *
 * @see BaseFragment
 *
 * @since 1.1
 * @author Clara Gros
 * @atuhor Babacar Toure
 */
public abstract class ProductListFragment extends BaseFragment {

    // Declare the RecyclerView
    RecyclerView recyclerView;
    // Declare the SwipeRefreshLayout
    SwipeRefreshLayout swipeRefreshLayout;
    // Declare the Floating Action Button
    FloatingActionButton buttonFab;

    // Declare the custom Adapter
    protected ProductPagedListAdapter adapter;

    // --------------
    // BASE METHODS
    // --------------

    @Override
    protected void updateDesign() {}

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_list;
    }

    @Override
    protected void configureDesign(View view) {
        // Bind views
        recyclerView = view.findViewById(R.id.products_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.fragment_product_list_swipe_container);
        buttonFab = view.findViewById(R.id.button_add_product);

        // Set on click listeners
        buttonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the Add Activity
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        // Call during UI creation
        this.configureRecyclerView();

        // Configure the SwipeRefreshLayout
        this.configureSwipeRefreshLayout();

    }

    // -----------------
    // CONFIGURATION
    // -----------------

    /**
     * Configure RecyclerView, Adapter, LayoutManager & glue it together
     */
    protected void configureRecyclerView() {

        // Create adapter
        adapter = new ProductPagedListAdapter(getContext());
        // Attach the adapter to the recycler view to populate items
        recyclerView.setAdapter(adapter);

        // Asserts the height (or width) of the item won't change
        // If you dont set this it will check if the size of the item has changed and that's expensive
        recyclerView.setHasFixedSize(true);

        // Allows to position all the data in the list correctly.
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Configure item click on RecyclerView
        ItemClickSupport.addTo(recyclerView, R.layout.product_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                                            @Override
                                            public void onItemClicked(RecyclerView recyclerView,
                                                                      int position,
                                                                      View v) {
                                                // Get product from adapter
                                                Product product = adapter.getItem(position);
                                                click(product);
                                            }
                                        }

                );
    }

    /**
     * To be defined in the fragments inherited from this ProductListFragment class
     * @param product (Product) the item clicked on
     */
    protected abstract void click(Product product);

    /**
     * Configure the SwipeRefreshLayout
     * <p>
     * Method to to add a listener to the SwipeRefreshLayout.
     * The latter will be launched when the user performs a "Pull To Refresh" and triggers the
     * onRefresh() method which will launch our usual stream.
     **/
    protected abstract void configureSwipeRefreshLayout();

    /**
     *
      */
    protected abstract void getProducts();

}