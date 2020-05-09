package com.example.cshare.ui.activities.productFragment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cshare.data.models.Product;
import com.example.cshare.R;
import com.squareup.picasso.Picasso;

public class ProductPagedListAdapter extends PagedListAdapter<Product, ProductPagedListAdapter.ProductViewHolder> {

    private Context context;

    public ProductPagedListAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        //@BindView(R.id.fragment_product_list_item_name)
        TextView textViewProductName;
        TextView textViewStatus;
        ImageView imageViewProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewStatus = itemView.findViewById(R.id.product_list_item_status);
            textViewProductName = itemView.findViewById(R.id.product_list_item_name);
            imageViewProduct = itemView.findViewById(R.id.product_list_item_image);
        }

        // Public method that will allow us to modify this view based on a Product object passed
        // as a parameter (the name of each Product product will be displayed in the TextView).
        public void updateWithProduct(Product product){

            this.textViewProductName.setText(product.getName());
            this.textViewStatus.setText(product.getStatus());

            switch(product.getStatus()) {
                case "Available":
                    textViewStatus.setTextColor(Color.parseColor("#5CB85C"));
                    break;
                case "Collected":
                    textViewStatus.setTextColor(Color.parseColor("#f0960c"));
                    break;
                case "Delivered":
                    textViewStatus.setTextColor(Color.parseColor("#f0230c"));
                    break;
                default:
            }
            Picasso.get().load(product.getProduct_picture()).into(imageViewProduct);
        }

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
