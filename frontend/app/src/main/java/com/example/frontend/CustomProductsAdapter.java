package com.example.frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.frontend.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomProductsAdapter extends ArrayAdapter<Product> {

    private ArrayList<Product> productArrayList;
    private Context mContext;

    // View lookup cache
    private static class ProductViewHolder {
        TextView textViewProductName;
        ImageView imageViewProduct;

        public ProductViewHolder(TextView textViewProductName, ImageView imageViewProduct){
            this.textViewProductName = textViewProductName;
            this.imageViewProduct = imageViewProduct;
        }
    }

    public CustomProductsAdapter(ArrayList<Product> productArrayList, Context context) {
        super(context, R.layout.product_row_item, productArrayList);
        this.productArrayList = productArrayList;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ProductViewHolder productViewHolder; // view lookup cache stored in tag

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.product_row_item, parent, false);

            TextView textViewProductName = convertView.findViewById(R.id.textViewProductName);
            ImageView imageViewProduct = (ImageView) convertView.findViewById(R.id.imageViewProduct);

            convertView.setTag(new ProductViewHolder(textViewProductName, imageViewProduct));

        }
       else {
            productViewHolder = (ProductViewHolder) convertView.getTag();
        }

        // Get the data item for this position
        Product product = getItem(position);

        productViewHolder = (ProductViewHolder) convertView.getTag();

        //Picasso.get().load(product.getProduct_picture()).into(productViewHolder.imageViewProduct);
        Picasso.get().load(product.getProduct_picture()).into(productViewHolder.imageViewProduct);
        productViewHolder.textViewProductName.setText(product.getName());


        return convertView;
    }

}
