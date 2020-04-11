package com.example.cshare.Controllers.Fragments;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cshare.Models.Product;
import com.example.cshare.R;
import com.example.cshare.Utils.Constants;
import com.example.cshare.Utils.ItemClickSupport;
import com.example.cshare.Views.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class
ProductListFragment extends BaseFragment {

    // FOR DESIGN
    // Declare RecyclerView
    RecyclerView recyclerView;
    // Declare the SwipeRefreshLayout
    SwipeRefreshLayout swipeRefreshLayout;

    //FOR DATA
    // protected Disposable disposable;
    // Declare list of products (Product) & Adapter
    private List<Product> products;
    protected ProductAdapter adapter;

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

        // Call during UI creation
        this.configureRecyclerView();

        // Configure the SwipeRefreshLayout
        this.configureSwipeRefreshLayout();
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
        this.products = new ArrayList<Product>();
        // Create adapter passing the list of users
        this.adapter = new ProductAdapter(this.products);
        // Attach the adapter to the recycler view to populate items
        this.recyclerView.setAdapter(this.adapter);
        // Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                                                Log.d(Constants.TAG, product.getName());
                                                click(product);
                                            }
                                        }

                );
    }

    protected abstract void click(Product product);

    // Configure SwipeRefreshLayout
    protected abstract void configureSwipeRefreshLayout();

}