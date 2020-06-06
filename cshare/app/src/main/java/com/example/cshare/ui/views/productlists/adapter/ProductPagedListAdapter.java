package com.example.cshare.ui.views.productlists.adapter;

import android.content.Context;
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

import com.example.cshare.R;
import com.example.cshare.data.models.Product;
import com.example.cshare.utils.Constants;
import com.squareup.picasso.Picasso;

import static com.example.cshare.R.color;
import static com.example.cshare.R.id;
import static com.example.cshare.R.layout;

/**
 * Adapter class for our RecyclerView that fetches the data page wise.
 * <p>
 * It inherits from the PagedListAdapter and has the common behaviours needed for a PagedList
 * for example Item Counting, Page CallBack etc. It helps us to load data gradually and gracefully
 * in our application’s RecyclerView.
 *
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 * @see RecyclerView.Adapter
 * @see PagedListAdapter
 */
public class ProductPagedListAdapter extends PagedListAdapter<Product,
        ProductPagedListAdapter.ProductViewHolder> {

    private Context context;

    /**
     * PagedListAdapter constructor
     * <p>
     * The adapter is almost same as RecyclerView.Adapter<>, the only change here is we have a
     * DIFF_CALLBACK implementation that we are passing to the super().
     * @param context (Context)
     * @see DiffUtil
     */
    public ProductPagedListAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    /**
     * ViewHolder class inheriting from RecyclerView.ViewHolder.
     * The purpose here is to model the previously created XML view product_list_item.xml
     * (representing a line in the RecyclerView) into a JAVA object.
     *
     * @since 1.0
     * @author Clara Gros
     * @author Babacar Toure
     * @see RecyclerView.ViewHolder
     */
    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewProductName;
        TextView textViewStatus;
        TextView textViewQuantity;
        TextView textViewExpirationDate;
        ImageView imageViewProduct;

        /**
         * View holder constructor
         */
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewStatus = itemView.findViewById(id.product_list_item_status);
            textViewProductName = itemView.findViewById(id.product_list_item_name);
            textViewQuantity = itemView.findViewById(id.product_list_item_quantity);
            textViewExpirationDate = itemView.findViewById(id.product_list_item_expiration_date);
            imageViewProduct = itemView.findViewById(id.product_list_item_image);
        }

        /**
         * Public method that enables to modify the item view (one row) to fill in the item
         * information in a row based on a Product object passed as a parameter.
         * @param product (Product) Product information to be displayed on a single item of the
         *                RecyclerView
         * @see Product
         */
        public void updateWithProduct(Product product){

            // Set product name and product status into TextViews
            this.textViewProductName.setText(product.getName());
            this.textViewStatus.setText(product.getStatus());
            this.textViewQuantity.setText(product.getQuantity());
            this.textViewExpirationDate.setText(product.getExpiration_date());

            // Edit text color according to the status
            switch(product.getStatus()) {
                case Constants.AVAILABLE:
                    textViewStatus.setTextColor(context.getColor(color.colorAvailable));
                    break;
                case Constants.COLLECTED:
                    textViewStatus.setTextColor(context.getColor(color.colorCollected));
                    break;
                case Constants.DELIVERED:
                    textViewStatus.setTextColor(context.getColor(color.colorDelivered));
                    break;
                default:
            }
            // Load the product picture in the ImageView
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
        View view = LayoutInflater.from(context).inflate(layout.product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Update view holder with a product
        holder.updateWithProduct(getItem(position));
    }

    /**
     * This callback is used to differentiate two items in a List.
     */
    private static DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getId() == newItem.getId();
        }

    };

}
