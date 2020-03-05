package com.example.cshare.Controllers.Fragments;

import android.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cshare.Models.Product;
import com.example.cshare.R;
import com.example.cshare.Utils.ApiStreams;
import com.example.cshare.Views.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public abstract class ProductListFragment extends BaseFragment {

    // FOR DESIGN
    // Declare RecyclerView
    @BindView(R.id.products_recycler_view)
    RecyclerView recyclerView;

    //FOR DATA
    protected Disposable disposable;
    // Declare list of products (Product) & Adapter
    private List<Product> products;
    private ProductAdapter adapter;
    // Declare the SwipeRefreshLayout
    @BindView(R.id.fragment_product_list_swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

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
    protected void configureDesign() {
        // Call during UI creation
        this.configureRecyclerView();

        // Configure the SwipeRefreshLayout
        this.configureSwipeRefreshLayout();

        // Execute stream after UI creation
        this.HttpRequest();
    }

    @Override
    protected void updateDesign() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    // Configure RecyclerView, Adapter, LayoutManager & glue it together
    private void configureRecyclerView() {
        // Reset list
        this.products = new ArrayList<>();
        // Create adapter passing the list of users
        this.adapter = new ProductAdapter(this.products);
        // Attach the adapter to the recycler view to populate items
        this.recyclerView.setAdapter(this.adapter);
        // Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    // Configure the SwipeRefreshLayout
    // We create a method that will allow us to configure our SwipeRefreshLayout and especially
    // to add a listener to it. The latter will be launched when the user performs a
    // "Pull To Refresh" and triggers the onRefresh() method which will launch our usual stream.
    private void configureSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HttpRequest();
            }
        });
    }

    // ------------------------------
    //  HTTP REQUEST (RxJAVA)
    // ------------------------------

    protected abstract void HttpRequest();

    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    // ------------------
    //  UPDATE UI
    // ------------------

    // Stop the SwipeRefreshLayout animation once our network request has ended correctly
    // ( setRefreshing(false) ). Then, we erase each time completely the previous list of products
    // ( products.clear() ) to avoid duplicating it because of the .addAll() .
    protected void updateUI(List<Product> newProducts) {
        // Stop refreshing and clear actual list of users
        swipeRefreshLayout.setRefreshing(false);
        products.clear();
        products.addAll(newProducts);
        adapter.notifyDataSetChanged();
    }

}