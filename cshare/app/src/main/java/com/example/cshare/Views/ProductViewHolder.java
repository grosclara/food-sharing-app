package com.example.cshare.Views;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cshare.Models.Product;
import com.example.cshare.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

// The purpose here is to model the previously created XML view (representing a line
// in the RecyclerView) into a JAVA object.

public class ProductViewHolder extends RecyclerView.ViewHolder {

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

        Log.i("img", product.getProduct_picture());
        Picasso.get().load(product.getProduct_picture()).into(imageViewProduct);
    }

}
