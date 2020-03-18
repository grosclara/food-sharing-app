package com.example.cshare.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cshare.Models.Product;
import com.example.cshare.R;

import java.util.List;

// We have created an Adapter, ProductAdapter, inheriting from RecyclerView.Adapter,
// which will allow us to link the RecyclerView view to our controller (ProductListFragment).

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    // FOR DATA
    private List<Product> products;

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
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_list_item, parent, false);

        return new ProductViewHolder(view);
    }

    // This method is called for each of the visible lines displayed in our RecyclerView.
    // This is where we update their appearance. In our case we call the ViewHolder method
    // we previously created, in order to update its TextView from a Product.
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Update view holder with a product
        holder.updateWithProduct(this.products.get(position));
    }

    @Override
    public int getItemCount() {
        // Return the total count of items in the list

        if (this.products == null) {
            return 0;
        } else {
            return this.products.size();
        }
    }

    public void updateProducts(List<Product> products){
        /**
         * If the product list has changed, update it
         */
        this.products = products;
        notifyDataSetChanged();
    }
}
