package com.example.cshare.Utils.ViewUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.example.cshare.Models.Product;
import com.example.cshare.R;

public class ProductPagedListAdapter extends PagedListAdapter<Product, ProductViewHolder> {

    private Context context;

    public ProductPagedListAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create view holder and inflating its xml layout
        View view = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Update view holder with a product
        holder.updateWithProduct(getItem(position));
    }

    private static DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            // TODO Override this equals method for product
            //return Objects.equals(oldItem, newItem);
            return oldItem.getId() == newItem.getId();
        }

    };

}
