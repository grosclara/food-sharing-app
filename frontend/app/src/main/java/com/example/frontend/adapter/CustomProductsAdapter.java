package com.example.frontend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frontend.R;
import com.example.frontend.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * CustomProductsAdapter extends ArrayAdapter and it is very flexible.
 * It allows us to display in a list view the information about a product the way we want according to
 * the product_row_item xml file that indicates the locations of the different information in a cell.
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 * @see res/layout/product_row_item
 */

// BETTER USE A RECYCLER VIEW ?????
public class CustomProductsAdapter extends ArrayAdapter<Product> {

    private LayoutInflater inflater;

    /**
     * Constructor that received two arguments in param that are saved in private variables as they are used to inflate and add data into the view.
     *
     * @param productArrayList
     * @param context
     */
    public CustomProductsAdapter(ArrayList<Product> productArrayList, Context context) {
        super(context, R.layout.product_row_item, productArrayList);
        // Create the inflater that will later on populate the view
        this.inflater = LayoutInflater.from(context);
    }

    // View lookup cache
    static class ProductViewHolder {
        private TextView textViewProductName;
        private ImageView imageViewProduct;

        /**
         * Constructor to instantiate a viewHolder object, which is an object of a class that can store the widgets present in the layout.
         *
         * @param textViewProductName
         * @param imageViewProduct
         */

        public ProductViewHolder(TextView textViewProductName, ImageView imageViewProduct) {
            this.textViewProductName = textViewProductName;
            this.imageViewProduct = imageViewProduct;
        }
    }

    /**
     * This method returns a view inflated with the data.
     * The convertView variable will hold all the rows for our ListView.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ProductViewHolder productViewHolder; // view lookup cache stored in tag

        // Check if the view we are trying to inflate is new or reused.
        if (convertView == null) {

            // The View is inflated using the inflate() method that takes into param the reference of our xml file
            // The parent and false at the end mean the root ViewGroup, which is the parent Layout, and whether we want to attach it to this root, which we don't.
            convertView = inflater.inflate(R.layout.product_row_item, parent, false);

            // Set the data into the views.
            TextView textViewProductName = convertView.findViewById(R.id.textViewProductName);
            ImageView imageViewProduct = convertView.findViewById(R.id.imageViewProduct);

            // Save the viewHolder into the memory of that convertView(setTag).
            convertView.setTag(new ProductViewHolder(textViewProductName, imageViewProduct));

        }

        // Get the data item for this position
        Product product = getItem(position);

        // Retrieve the view holder object from convertView every time getView() is called.
        productViewHolder = (ProductViewHolder) convertView.getTag();

        // Update the widgets inside it.
        productViewHolder.textViewProductName.setText(product.getName());
        Picasso.get().load(product.getProduct_picture()).into(productViewHolder.imageViewProduct);

        return convertView;
    }

}
