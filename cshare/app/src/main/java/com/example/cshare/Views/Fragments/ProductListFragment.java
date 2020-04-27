package com.example.cshare.Views.Fragments;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.cshare.Models.Product;
import com.example.cshare.R;
import com.example.cshare.Utils.Constants;
import com.example.cshare.Utils.ViewUtils.ItemClickSupport;
import com.example.cshare.Utils.ViewUtils.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class
ProductListFragment extends BaseFragment {

    // FOR DESIGN
    // Declare RecyclerView
    RecyclerView recyclerView;
    // Declare progress bar
    ProgressBar progressBar;
    // Declare the SwipeRefreshLayout
    SwipeRefreshLayout swipeRefreshLayout;

    //FOR DATA
    // protected Disposable disposable;
    // Declare list of products (Product) & Adapter
    private List<Product> products;
    protected ProductAdapter adapter;

    protected Boolean isClickable = true;

    // --------------
    // BASE METHODS
    // --------------

    @Override
    protected abstract BaseFragment newInstance();

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_list;
    }

    @Override
    protected void configureDesign(View view) {
        // Bind views
        recyclerView = view.findViewById(R.id.products_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.fragment_product_list_swipe_container);
        progressBar = view.findViewById(R.id.progressBar);

        // Call during UI creation
        this.configureRecyclerView();

        // Configure the SwipeRefreshLayout
        this.configureSwipeRefreshLayout();

        this.configureProgressBar();
    }

    @Override
    protected void updateDesign() {
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    // Configure RecyclerView, Adapter, LayoutManager & glue it together
    protected void configureRecyclerView() {
        //recyclerView = findViewById
        // Reset list
        products = new ArrayList<Product>();
        // Create adapter passing the list of users
        adapter = new ProductAdapter(products);
        // Attach the adapter to the recycler view to populate items
        recyclerView.setAdapter(adapter);
        // setHasFixedSize makes sure that this change of size of RecyclerView is constant.
        // The height (or width) of the item won't change.
        // Every item added or removed will be the same.
        // If you dont set this it will check if the size of the item has changed and that's expensive
        recyclerView.setHasFixedSize(true);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        configureOnItemClickedRecyclerView();

    }

    protected void configureOnItemClickedRecyclerView() {

        // Configure item click on RecyclerView
        ItemClickSupport.addTo(recyclerView, R.layout.product_list_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                                            @Override
                                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                                // Get product from adapter
                                                Product product = adapter.getProduct(position);
                                                click(product);
                                            }
                                        }

                );
    }

    protected abstract void click(Product product);

    // Configure SwipeRefreshLayout
    protected abstract void configureSwipeRefreshLayout();

    protected abstract void configureProgressBar();

}