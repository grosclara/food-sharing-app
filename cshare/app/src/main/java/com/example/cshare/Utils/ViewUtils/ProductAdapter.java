package com.example.cshare.Utils.ViewUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cshare.Models.Product;
import com.example.cshare.R;

import java.util.ArrayList;
import java.util.List;

// We have created an Adapter, ProductAdapter, inheriting from RecyclerView.Adapter,
// which will allow us to link the RecyclerView view to our controller (ProductListFragment).

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    // FOR DATA
    private List<Product> products = new ArrayList<>();

    // CONSTRUCTOR
    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    // Allows you to create a ViewHolder from the XML layout representing each line of the
    // RecyclerView. This will be called for the first visible lines of the RecyclerView.
    // The RecyclerView has a system to reuse ViewHolders that have already been created.
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create view holder and inflating its xml layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    // This method is called for each of the visible lines displayed in our RecyclerView.
    // This is where we update their appearance. In our case we call the ViewHolder method
    // we previously created, in order to update its TextView from a Product.
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Update view holder with a product
        holder.updateWithProduct(products.get(position));
    }

    @Override
    public int getItemCount() {
        // Return the total count of items in the list

        if (products == null) {
            return 0;
        } else {
            return products.size();
        }
    }

    public void setProducts(List<Product> newProducts){
        /**
         * If the product list has changed, update it
         */
        products = newProducts;
        notifyDataSetChanged();
    }

    // Create a method that allows to get the product from the position of the item clicked
    public Product getProduct(int position){
        return products.get(position);
    }
}
